package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Transaction;


import com.xplo.code.data.db.room.model.Address;
import com.xplo.code.data.db.room.model.Alternate;
import com.xplo.code.data.db.room.model.Beneficiary;
import com.xplo.code.data.db.room.model.Biometric;
import com.xplo.code.data.db.room.model.HouseholdInfo;
import com.xplo.code.data.db.room.model.Location;
import com.xplo.code.data.db.room.model.Nominee;
import com.xplo.code.data.db.room.model.SelectionReason;
import com.xplo.code.data.db.room.model.SyncBeneficiary;

import java.util.List;

@Dao
public abstract class BeneficiaryTransactionDao {
    @Transaction
    public void insertBeneficiaryRecord(Beneficiary beneficiary, Address address, Location location,
                                        List<Biometric> biometric,
                                        List<HouseholdInfo> householdInfo,
                                        List<Alternate> alternate,
                                        List<Nominee> nominee,
                                        List<SelectionReason> selectionReason
    ) {

        for (SelectionReason nowSelectionReason : selectionReason) {
            insert(nowSelectionReason);
        }
        for (Nominee nowNominee : nominee) {
            insert(nowNominee);
        }
        for (Alternate nowAlternate : alternate) {
            insert(nowAlternate);
        }
        for (HouseholdInfo nowHouseholdInfo : householdInfo) {
            insert(nowHouseholdInfo);
        }
        for (Biometric nowBiometric : biometric) {
            insert(nowBiometric);
        }

        insert(location);
        insert(address);
        insert(beneficiary);
    }

    @Transaction
    public void insertAlternateRecord(
            List<Biometric> biometric,
            List<Alternate> alternate
    ) {

        for (Alternate nowAlternate : alternate) {
            insert(nowAlternate);
        }
        for (Biometric nowBiometric : biometric) {
            insert(nowBiometric);
        }

    }

    @Insert
    abstract void insert(Beneficiary beneficiary);

    @Insert
    abstract void insert(Address address);

    @Insert
    abstract void insert(Location location);

    @Insert
    abstract void insert(Biometric biometric);

    @Insert
    abstract void insert(HouseholdInfo householdInfo);

    @Insert
    abstract void insert(Alternate alternate);

    @Insert
    abstract void insert(Nominee nominee);

    @Insert
    abstract void insert(SelectionReason selectionReason);

    @Insert
    abstract void insert(SyncBeneficiary syncBeneficiary);
}
