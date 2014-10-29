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
 * mysql> describe tag;
 * +------------+--------------+------+-----+-------------------+----------------+
 * | Field      | Type         | Null | Key | Default           | Extra          |
 * +------------+--------------+------+-----+-------------------+----------------+
 * | id         | int(11)      | NO   | PRI | NULL              | auto_increment |
 * | owner      | int(11)      | YES  | MUL | NULL              |                |
 * | tag        | varchar(255) | YES  |     | NULL              |                |
 * | createdate | timestamp    | NO   |     | CURRENT_TIMESTAMP |                |
 * | deleted    | tinyint(1)   | YES  |     | NULL              |                |
 * +------------+--------------+------+-----+-------------------+----------------+
 * 5 rows in set (0.03 sec)
 */
case class Tag(id: Long, owner: Long, tag: String, createdate: Date, deleted: Boolean)

object Tag {

  val simple = {
    get[Long]("tag.id") ~
      get[Long]("tag.owner") ~
      get[String]("tag.tag") ~
      get[Date]("tag.createdate") ~
      get[Boolean]("tag.deleted") map {
        case id ~ owner ~ tag ~ createdate ~ deleted => Tag(id, owner, tag, createdate, deleted)
      }
  }

  def findAll(owner: Long): Seq[Tag] = {
    DB.withConnection { implicit connection =>
      SQL("select id, owner, tag, createdate, deleted from tag where owner = {owner}").on(
        'owner -> owner).as(Tag.simple *)
    }
  }

  /** Retrieve all done items for a given email (owner) and doneDay 
  def findByDoneDay(email: String, doneday: Long): Seq[Done] = {
    println (email + " " + doneday + " .... ")
    DB.withConnection { implicit connection =>
      SQL("select d.id, d.owner, d.donetext, d.donedate, d.createdate, d.deleted, d.category, d.doneDay " 
          + " from done d join user u where d.owner = u.id and d.deleted = 0 " 
          + " and u.email = {email} and d.doneDay = {doneDay}").on(
        'email -> email,
        'doneDay -> doneday).as(Done.simple *)
    }
  }*/

  /** Create a Done item.
  def create(done: Done): Done = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into done (owner, donetext, donedate, createdate, deleted, category, doneDay) values (
          {owner}, {donetext}, {donedate}, {createdate}, {deleted}, {category}, {doneDay}
          )
        """).on(
          'owner -> done.owner,
          'donetext -> done.donetext,
          'donedate -> done.donedate,
          'createdate -> done.createdate,
          'deleted -> done.deleted,
          'category -> done.category,
          'doneDay -> done.doneDay).executeInsert()

      done
    }
  } */

  implicit val tagReads = Json.reads[Tag]
  implicit val tagWrites = Json.writes[Tag]

}

