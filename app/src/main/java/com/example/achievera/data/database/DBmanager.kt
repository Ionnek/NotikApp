package com.example.achievera.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.achievera.data.dao.linkedimagesDao
import com.example.achievera.data.dao.noteLinkDao
import com.example.achievera.data.dao.notesDao
import com.example.achievera.data.dao.tagsDao
import com.example.achievera.data.model.LinkedImage
import com.example.achievera.data.model.NoteLink
import com.example.achievera.data.model.NoteTagCrossRef
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.di.Converters

@Database(entities = [NotesDatabaseElement::class, NoteLink::class, Tag::class, LinkedImage::class,NoteTagCrossRef::class,], version = 4)
@TypeConverters(Converters::class)
abstract class DBmanager: RoomDatabase() {
    abstract fun tagsDao(): tagsDao
    abstract fun linkedimagesDao(): linkedimagesDao
    abstract fun notesDao(): notesDao
    abstract fun noteLinkDao(): noteLinkDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `NoteTagCrossRef` (
                        `noteId` INTEGER NOT NULL,
                        `tagId` INTEGER NOT NULL,
                        PRIMARY KEY(`noteId`, `tagId`)
                    )
                """.trimIndent())
                database.execSQL("""
                ALTER TABLE `tags` ADD COLUMN `isActived` INTEGER NOT NULL DEFAULT 1
            """.trimIndent())
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `NoteTagCrossRef_new` (
                `noteId` INTEGER NOT NULL,
                `tagId` INTEGER NOT NULL,
                PRIMARY KEY(`noteId`, `tagId`),
                FOREIGN KEY(`noteId`) REFERENCES `notes`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`tagId`) REFERENCES `tags`(`id`) ON DELETE CASCADE
            )
        """.trimIndent())


                database.execSQL("""
            INSERT INTO `NoteTagCrossRef_new` (`noteId`, `tagId`)
            SELECT `noteId`, `tagId` FROM `NoteTagCrossRef`
        """.trimIndent())

                database.execSQL("DROP TABLE `NoteTagCrossRef`")

                database.execSQL("ALTER TABLE `NoteTagCrossRef_new` RENAME TO `NoteTagCrossRef`")

                database.execSQL("CREATE INDEX IF NOT EXISTS `index_NoteTagCrossRef_noteId` ON `NoteTagCrossRef` (`noteId`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_NoteTagCrossRef_tagId` ON `NoteTagCrossRef` (`tagId`)")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    "ALTER TABLE `notes` " +
                            "ADD COLUMN `chekboxes` TEXT NOT NULL DEFAULT 'undefined'"
                )

            }
        }
    }
}