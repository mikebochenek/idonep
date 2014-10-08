package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

/**
 * mysql> describe item;
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
case class Done(id: Long, owner: Long, donetext: String, donedate: Long,
  createdate: Long, deleted: Boolean, category: Long, doneDay: Long)

object Done {

  /** Parse a Done from a ResultSet */
  val simple = {
    get[Long]("done.id") ~
      get[Long]("done.owner") ~
      get[String]("done.donetext") ~
      get[Long]("done.donedate") ~
      get[Long]("done.createdate") ~
      get[Boolean]("done.deleted") ~
      get[Long]("done.category") ~
      get[Long]("done.doneDay") map {
        case id ~ owner ~ donetext ~ donedate ~ createdate ~ deleted ~ category ~ doneDay 
          => Done(id, owner, donetext, donedate, createdate, deleted, category, doneDay)
      }
  }

}