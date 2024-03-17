package com.xplo.code.data.db.room.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.xplo.code.data.db.room.dao.*;
import com.xplo.code.data.db.room.model.*;

@Database(entities = {Beneficiary.class, Address.class, Location.class, Alternate.class,
        Nominee.class, Biometric.class, HouseholdInfo.class, SelectionReason.class, SyncBeneficiary.class},
        version = 4, exportSchema = false)
public abstract class BeneficiaryDatabase extends RoomDatabase {
    private static final String LOG_TAG = BeneficiaryDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "benedb.db";
    private static BeneficiaryDatabase sInstance;

    public static synchronized BeneficiaryDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                BeneficiaryDatabase.class, DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public static void dbCloseFromDB() {
        if (sInstance != null) {
            sInstance.close();
        }
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Perform migration from version 1 to 2
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Perform migration from version 2 to 3
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Beneficiary ADD COLUMN application_status INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE Beneficiary ADD COLUMN api_count INTEGER NOT NULL DEFAULT 0");

            database.execSQL("CREATE TABLE IF NOT EXISTS sync_beneficiary " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, application_id TEXT, beneficiary_name TEXT)");
        }
    };

    public abstract BeneficiaryDao beneficiaryDao();

    public abstract AddressDao addressDao();

    public abstract LocationDao locationDao();

    public abstract AlternateDao alternateDao();

    public abstract NomineeDao nomineeDao();

    public abstract BiometricDao biometricDao();

    public abstract HouseholdInfoDao householdInfoDao();

    public abstract SelectionReasonDao selectionReasonDao();

    public abstract BeneficiaryTransactionDao beneficiaryTransactionDao();

    public abstract SyncBeneficiaryDao syncBeneficiaryDao();
}
