package com.xplo.code.data.db.room.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.xplo.code.data.db.room.dao.AddressDao;
import com.xplo.code.data.db.room.dao.AlternateDao;
import com.xplo.code.data.db.room.dao.BeneficiaryDao;
import com.xplo.code.data.db.room.dao.BeneficiaryTransactionDao;
import com.xplo.code.data.db.room.dao.BiometricDao;
import com.xplo.code.data.db.room.dao.HouseholdInfoDao;
import com.xplo.code.data.db.room.dao.LocationDao;
import com.xplo.code.data.db.room.dao.NomineeDao;
import com.xplo.code.data.db.room.dao.SelectionReasonDao;
import com.xplo.code.data.db.room.model.Address;
import com.xplo.code.data.db.room.model.Alternate;
import com.xplo.code.data.db.room.model.Beneficiary;
import com.xplo.code.data.db.room.model.Biometric;
import com.xplo.code.data.db.room.model.HouseholdInfo;
import com.xplo.code.data.db.room.model.Location;
import com.xplo.code.data.db.room.model.Nominee;
import com.xplo.code.data.db.room.model.SelectionReason;


@Database(entities = {Beneficiary.class, Address.class, Location.class, Alternate.class,
        Nominee.class, Biometric.class, HouseholdInfo.class, SelectionReason.class},
        version = 2, exportSchema = false)
public abstract class BeneficiaryDatabase extends RoomDatabase {
    private static final String LOG_TAG = BeneficiaryDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "benedb.db";
    private static BeneficiaryDatabase sInstance;

    public static BeneficiaryDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                try {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                    BeneficiaryDatabase.class, BeneficiaryDatabase.DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)  // Add your migration here
                            .build();
                } catch (Exception exc) {
                    Log.d(LOG_TAG, "Error while creating new database instance");
                    exc.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    // Define your migration
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Perform the necessary schema changes here
            // For example, you can use SQL queries to add or modify tables/columns
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
}
