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
 * mysql> describe donetag;
 * +------------+------------+------+-----+-------------------+----------------+
 * | Field      | Type       | Null | Key | Default           | Extra          |
 * +------------+------------+------+-----+-------------------+----------------+
 * | id         | int(11)    | NO   | PRI | NULL              | auto_increment |
 * | tagid      | int(11)    | YES  | MUL | NULL              |                |
 * | doneid     | int(11)    | YES  | MUL | NULL              |                |
 * | createdate | timestamp  | NO   |     | CURRENT_TIMESTAMP |                |
 * | deleted    | tinyint(1) | YES  |     | NULL              |                |
 * +------------+------------+------+-----+-------------------+----------------+
 * 5 rows in set (0.00 sec)
 *
 */
case class DoneTag(id: Long, tagid: Long, doneid: Long, createdate: Date, deleted: Boolean)

object DoneTag {
    val simple = {
    get[Long]("donetag.id") ~
      get[Long]("donetag.tagid") ~
      get[Long]("donetag.doneid") ~
      get[Date]("donetag.createdate") ~
      get[Boolean]("donetag.deleted") map {
        case id ~ tagid ~ doneid ~ createdate ~ deleted => DoneTag(id, tagid, doneid, createdate, deleted)
      }
  }

  def findAll(owner: Long): Seq[DoneTag] = {
    DB.withConnection { implicit connection =>
      SQL("select id, tagid, doneid, createdate, deleted from donetag").as(DoneTag.simple *)
    }
  }

  def create(tag: DoneTag): Option[Long] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into donetag (tagid, doneid, deleted) values (
          {tagid}, {doneid}, {deleted}
          )
        """).on(
          'tagid -> tag.tagid,
          'doneid -> tag.doneid,
          'deleted -> 0).executeInsert()
     }
  }

  implicit val doneTagReads = Json.reads[DoneTag]
  implicit val doneTagWrites = Json.writes[DoneTag]

}