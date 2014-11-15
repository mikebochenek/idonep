package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date
import scala.language.postfixOps
import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * mysql> describe team;
 * +------------+------------+------+-----+-------------------+----------------+
 * | Field      | Type       | Null | Key | Default           | Extra          |
 * +------------+------------+------+-----+-------------------+----------------+
 * | id         | int(11)    | NO   | PRI | NULL              | auto_increment |
 * | owner      | int(11)    | YES  | MUL | NULL              |                |
 * | target     | int(11)    | YES  | MUL | NULL              |                |
 * | createdate | timestamp  | NO   |     | CURRENT_TIMESTAMP |                |
 * | deleted    | tinyint(1) | YES  |     | NULL              |                |
 * | status     | tinyint(1) | YES  |     | NULL              |                |
 * +------------+------------+------+-----+-------------------+----------------+
 * 6 rows in set (0.01 sec)
 */
case class Team(id: Long, owner: Long, target: Long, createdate: Date, deleted: Int, status: Int)

object Team {
  val simple = {
    get[Long]("team.id") ~
      get[Long]("team.owner") ~
      get[Long]("team.target") ~
      get[Date]("team.createdate") ~
      get[Int]("team.deleted") ~
      get[Int]("team.status") map {
        case id ~ owner ~ target ~ createdate ~ deleted ~ status => Team(id, owner, target, createdate, deleted, status)
      }
  }

  def create(ownerId: Long, targetId: Long): Option[Long] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into team (owner, target, createdate, deleted, status) values (
          {owner}, {target}, {createdate}, {deleted}, {status}
          )
        """).on(
          'owner -> ownerId,
          'target -> targetId,
          'createdate -> new Date(),
          'deleted -> 0,
          'status -> 0).executeInsert()
    }
  }

  def findMyTargets(owner: Long, status: Int): Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select u.id, u.email, u.username, u.password from user u "
    		  + " join team t where u.id = t.target and t.owner = {owner} " 
    		  + " and t.status = {status} and t.deleted = 0 ").on(
        'owner -> owner, 'status -> status).as(User.simple *)
    }
  }

  def findMyOwners(target: Long, status: Int): Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select u.id, u.email, u.username, u.password from user u "
    		  + " join team t where u.id = t.owner and t.target = {target} " 
    		  + " and t.status = {status} and t.deleted = 0 ").on(
        'target -> target, 'status -> status).as(User.simple *)
    }
  }

  def updatestatus(owner: Long, target: Long, status: Int) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         update team set status = {status} where 
         owner = {owner} and target = {target}
        """).on(
          'owner -> owner,
          'target -> target,
          'status -> status).executeUpdate
    }
  }
  
  
  implicit val teamReads = Json.reads[Team]
  implicit val teamWrites = Json.writes[Team]

}