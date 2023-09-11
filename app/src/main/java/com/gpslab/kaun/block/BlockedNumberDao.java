package com.gpslab.kaun.block;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BlockedNumberDao {
    @Query("SELECT * FROM blockednumbers")
    List<BlockedNumber> getAll();

    @Insert
    void insert(BlockedNumber number);

    @Delete
    void delete(BlockedNumber number);
}
