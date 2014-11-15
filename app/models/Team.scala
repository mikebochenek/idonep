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

  implicit val teamReads = Json.reads[Team]
  implicit val teamWrites = Json.writes[Team]

}