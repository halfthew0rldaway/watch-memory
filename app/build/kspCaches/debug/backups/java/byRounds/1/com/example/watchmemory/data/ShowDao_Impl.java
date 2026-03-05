package com.example.watchmemory.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ShowDao_Impl implements ShowDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ShowEntity> __insertionAdapterOfShowEntity;

  private final EntityDeletionOrUpdateAdapter<ShowEntity> __deletionAdapterOfShowEntity;

  private final EntityDeletionOrUpdateAdapter<ShowEntity> __updateAdapterOfShowEntity;

  public ShowDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfShowEntity = new EntityInsertionAdapter<ShowEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `shows` (`id`,`title`,`episode`,`totalEpisodes`,`category`,`status`,`note`,`lastUpdated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShowEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getEpisode());
        if (entity.getTotalEpisodes() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTotalEpisodes());
        }
        statement.bindString(5, entity.getCategory());
        statement.bindString(6, entity.getStatus());
        statement.bindString(7, entity.getNote());
        statement.bindLong(8, entity.getLastUpdated());
      }
    };
    this.__deletionAdapterOfShowEntity = new EntityDeletionOrUpdateAdapter<ShowEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `shows` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShowEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfShowEntity = new EntityDeletionOrUpdateAdapter<ShowEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `shows` SET `id` = ?,`title` = ?,`episode` = ?,`totalEpisodes` = ?,`category` = ?,`status` = ?,`note` = ?,`lastUpdated` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShowEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getEpisode());
        if (entity.getTotalEpisodes() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTotalEpisodes());
        }
        statement.bindString(5, entity.getCategory());
        statement.bindString(6, entity.getStatus());
        statement.bindString(7, entity.getNote());
        statement.bindLong(8, entity.getLastUpdated());
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insertShow(final ShowEntity show, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfShowEntity.insertAndReturnId(show);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteShow(final ShowEntity show, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfShowEntity.handle(show);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateShow(final ShowEntity show, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfShowEntity.handle(show);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ShowEntity>> getAllShows() {
    final String _sql = "SELECT * FROM shows ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"shows"}, new Callable<List<ShowEntity>>() {
      @Override
      @NonNull
      public List<ShowEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfEpisode = CursorUtil.getColumnIndexOrThrow(_cursor, "episode");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEpisodes");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<ShowEntity> _result = new ArrayList<ShowEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ShowEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpEpisode;
            _tmpEpisode = _cursor.getInt(_cursorIndexOfEpisode);
            final Integer _tmpTotalEpisodes;
            if (_cursor.isNull(_cursorIndexOfTotalEpisodes)) {
              _tmpTotalEpisodes = null;
            } else {
              _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new ShowEntity(_tmpId,_tmpTitle,_tmpEpisode,_tmpTotalEpisodes,_tmpCategory,_tmpStatus,_tmpNote,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getShowById(final long id, final Continuation<? super ShowEntity> $completion) {
    final String _sql = "SELECT * FROM shows WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ShowEntity>() {
      @Override
      @Nullable
      public ShowEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfEpisode = CursorUtil.getColumnIndexOrThrow(_cursor, "episode");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEpisodes");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final ShowEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpEpisode;
            _tmpEpisode = _cursor.getInt(_cursorIndexOfEpisode);
            final Integer _tmpTotalEpisodes;
            if (_cursor.isNull(_cursorIndexOfTotalEpisodes)) {
              _tmpTotalEpisodes = null;
            } else {
              _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new ShowEntity(_tmpId,_tmpTitle,_tmpEpisode,_tmpTotalEpisodes,_tmpCategory,_tmpStatus,_tmpNote,_tmpLastUpdated);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMostRecentShow(final Continuation<? super ShowEntity> $completion) {
    final String _sql = "SELECT * FROM shows ORDER BY lastUpdated DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ShowEntity>() {
      @Override
      @Nullable
      public ShowEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfEpisode = CursorUtil.getColumnIndexOrThrow(_cursor, "episode");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEpisodes");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final ShowEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpEpisode;
            _tmpEpisode = _cursor.getInt(_cursorIndexOfEpisode);
            final Integer _tmpTotalEpisodes;
            if (_cursor.isNull(_cursorIndexOfTotalEpisodes)) {
              _tmpTotalEpisodes = null;
            } else {
              _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new ShowEntity(_tmpId,_tmpTitle,_tmpEpisode,_tmpTotalEpisodes,_tmpCategory,_tmpStatus,_tmpNote,_tmpLastUpdated);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllShowsList(final Continuation<? super List<ShowEntity>> $completion) {
    final String _sql = "SELECT * FROM shows ORDER BY lastUpdated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ShowEntity>>() {
      @Override
      @NonNull
      public List<ShowEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfEpisode = CursorUtil.getColumnIndexOrThrow(_cursor, "episode");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEpisodes");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<ShowEntity> _result = new ArrayList<ShowEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ShowEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpEpisode;
            _tmpEpisode = _cursor.getInt(_cursorIndexOfEpisode);
            final Integer _tmpTotalEpisodes;
            if (_cursor.isNull(_cursorIndexOfTotalEpisodes)) {
              _tmpTotalEpisodes = null;
            } else {
              _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new ShowEntity(_tmpId,_tmpTitle,_tmpEpisode,_tmpTotalEpisodes,_tmpCategory,_tmpStatus,_tmpNote,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
