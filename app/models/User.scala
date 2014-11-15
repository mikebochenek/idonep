package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import scala.language.postfixOps
import java.util.Date
import play.api.Logger

/**
 * mysql> describe user
 * -> ;
 * +---------------+--------------+------+-----+---------+-------+
 * | Field         | Type         | Null | Key | Default | Extra |
 * +---------------+--------------+------+-----+---------+-------+
 * | id            | bigint(20)   | NO   | PRI | NULL    |       |
 * | createdate    | datetime     | YES  |     | NULL    |       |
 * | lastlogindate | datetime     | YES  |     | NULL    |       |
 * | deleted       | bit(1)       | YES  |     | NULL    |       |
 * | password      | varchar(255) | YES  |     | NULL    |       |
 * | settings      | longtext     | YES  |     | NULL    |       |
 * | username      | varchar(255) | YES  |     | NULL    |       |
 * | email         | varchar(255) | YES  |     | NULL    |       |
 * | type          | varchar(255) | YES  |     | NULL    |       |
 * | openidtoken   | varchar(255) | YES  |     | NULL    |       |
 * +---------------+--------------+------+-----+---------+-------+
 * 10 rows in set (0.00 sec)
 *
 */

case class User(id: Long, email: String, username: String, password: String)

object User {

  /**
   * Parse a User from a ResultSet
   */
  val simple = {
    get[Long]("user.id") ~
      get[String]("user.email") ~
      get[String]("user.username") ~
      get[String]("user.password") map {
        case id ~ email ~ username ~ password => User(id, email, username, password)
      }
  }

  def findByEmail(email: String): User = {
    DB.withConnection { implicit connection =>
      SQL("select id, email, username, password from user where email = {email}").on(
        'email -> email).as(User.simple.single)
    }
  }

  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select id, email, username, password from user").as(User.simple *)
    }
  }
  
  def updatelastlogindate(email: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         update user set lastlogindate = {lastlogindate} where 
         email = {email} 
        """).on(
          'email -> email,
          'lastlogindate -> new Date()).executeUpdate
    }
  }

  def updatepassword(email: String, password: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        "update user set password = {password} where email = {email} ").on(
          'email -> email,
          'password -> hash(password)).executeUpdate
    }
  }
  
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select id, email, username, password from user where 
         email = {email} and password = {password}
        """).on(
          'email -> email,
          'password -> hash(password)).as(User.simple.singleOpt)
    }
  }

  def create(user: User): User = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user (email, username, password, createdate) values (
            {email}, {username}, {password}, {createdate}
          )
        """).on(
          'email -> user.email,
          'username -> user.username,
          'password -> hash(user.password),
          'createdate -> new Date()).executeInsert()

      user

    }
  }

  def create(email: String, password: String, password2: String) = {
    Logger.info ("creating user with email:" + email)
    
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user (email, username, password, createdate) values (
            {email}, {username}, {password}, {createdate}
          )
        """).on(
          'email -> email,
          'username -> email,
          'password -> hash(password),
          'createdate -> new Date()).executeInsert()

    }
  }

  
  def hash(str: String): String = {
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val ha = new sun.misc.BASE64Encoder().encode(md.digest((str + "i2.uZE92").getBytes))
    ha.toString()
  }

}
