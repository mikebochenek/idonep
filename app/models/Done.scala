package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

/**
 * mysql> describe done
 * -> ;
 * +------------+--------------+------+-----+---------+----------------+
 * | Field      | Type         | Null | Key | Default | Extra          |
 * +------------+--------------+------+-----+---------+----------------+
 * | id         | int(11)      | NO   | PRI | NULL    | auto_increment |
 * | owner      | int(11)      | YES  |     | NULL    |                |
 * | donetext   | varchar(255) | YES  |     | NULL    |                |
 * | donedate   | int(11)      | YES  |     | NULL    |                |
 * | createdate | int(11)      | YES  |     | NULL    |                |
 * | deleted    | tinyint(1)   | YES  |     | NULL    |                |
 * +------------+--------------+------+-----+---------+----------------+
 * 6 rows in set (0.04 sec)
 *
 */
case class Done(id: Long, owner: Long, donetext: String, donedate: Long, createdate: Long, deleted: Boolean)

object Done {
  // -- Parsers

  /**
   * Parse a Done from a ResultSet
   */
  val simple = {
      get[Long]("done.id") ~
      get[Long]("done.owner") ~
      get[String]("done.donetext") ~
      get[Long]("done.donedate") ~
      get[Long]("done.createdate") ~
      get[Boolean]("user.deleted") map {
        case id ~ owner ~ donetext ~ donedate ~ createdate ~ deleted => Done(id, owner, donetext, donedate, createdate, deleted)
      }
  }

}