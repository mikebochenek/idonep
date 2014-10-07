package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class User(id: Int, email: String, username: String, password: String)
/*
id	bigint(20) PK 
createdate	datetime 
lastlogindate	datetime 
deleted	bit(1) 
password	varchar(255) 
settings	longtext 
username	varchar(255) 
email	varchar(255) 
type	varchar(255) 
openidtoken	varchar(255) 
*/

object User {
  
  // -- Parsers
  
  /**
   * Parse a User from a ResultSet
   */
  val simple = {
    get[Int]("user.id") ~
    get[String]("user.email") ~
    get[String]("user.username") ~
    get[String]("user.password") map {
      case id~email~username~password => User(id, email, username, password)
    }
  }
  
  // -- Queries
  
  /**
   * Retrieve a User from email.
   */
  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select id, email, username, password from user where email = {email}").on(
        'email -> email
      ).as(User.simple.singleOpt)
    }
  }
  
  /**
   * Retrieve all users.
   */
  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select id, email, username, password from user").as(User.simple *)
    }
  }
  
  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select id, email, username, password from user where 
         email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.simple.singleOpt)
    }
  }
   
  /**
   * Create a User.
   */
  def create(user: User): User = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user values (
            {email}, {username}, {password}
          )
        """
      ).on(
        'email -> user.email,
        'name -> user.username,
        'password -> user.password
      ).executeUpdate()
      
      user
      
    }
  }
  
}
