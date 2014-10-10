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
 * mysql> describe done;
 * +------------+------------+------+-----+-------------------+----------------+
 * | Field      | Type       | Null | Key | Default           | Extra          |
 * +------------+------------+------+-----+-------------------+----------------+
 * | id         | int(11)    | NO   | PRI | NULL              | auto_increment |
 * | owner      | int(11)    | YES  |     | NULL              |                |
 * | donetext   | text       | YES  |     | NULL              |                |
 * | donedate   | datetime   | YES  |     | NULL              |                |
 * | createdate | timestamp  | NO   |     | CURRENT_TIMESTAMP |                |
 * | deleted    | tinyint(1) | YES  |     | NULL              |                |
 * | category   | bigint(20) | NO   |     | NULL              |                |
 * | doneDay    | bigint(20) | NO   |     | NULL              |                |
 * +------------+------------+------+-----+-------------------+----------------+
 * 8 rows in set (0.00 sec)
 */
case class Done(id: Long, owner: Long, donetext: String, donedate: Date,
  createdate: Date, deleted: Boolean, category: Long, doneDay: Long)

object Done {

  /** Parse a Done from a ResultSet */
  val simple = {
    get[Long]("done.id") ~
      get[Long]("done.owner") ~
      get[String]("done.donetext") ~
      get[Date]("done.donedate") ~
      get[Date]("done.createdate") ~
      get[Boolean]("done.deleted") ~
      get[Long]("done.category") ~
      get[Long]("done.doneDay") map {
        case id ~ owner ~ donetext ~ donedate ~ createdate ~ deleted ~ category ~ doneDay => Done(id, owner, donetext, donedate, createdate, deleted, category, doneDay)
      }
  }

  /** Retrieve all done items for a given ownerID. */
  def findAll(owner: Long): Seq[Done] = {
    DB.withConnection { implicit connection =>
      SQL("select id, owner, donetext, donedate, createdate, deleted, category, doneDay from done where owner = {owner}").on(
        'owner -> owner).as(Done.simple *)
    }
  }

  /** Create a Done item. */
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
  }

  implicit val doneReads = Json.reads[Done]
  implicit val doneWrites = Json.writes[Done]

}

