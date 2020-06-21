package com.sparrowwallet.sparrow.wallet;

import com.sparrowwallet.drongo.wallet.Wallet;
import com.sparrowwallet.sparrow.EventManager;
import com.sparrowwallet.sparrow.event.WalletSettingsChangedEvent;
import com.sparrowwallet.sparrow.io.Storage;

import java.io.IOException;

public class SettingsWalletForm extends WalletForm {
    private Wallet walletCopy;

    public SettingsWalletForm(Storage storage, Wallet currentWallet) {
        super(storage, currentWallet);
        this.walletCopy = currentWallet.copy();
    }

    @Override
    public Wallet getWallet() {
        return walletCopy;
    }

    @Override
    public void revert() {
        this.walletCopy = super.getWallet().copy();
    }

    @Override
    public void saveAndRefresh() throws IOException {
        //TODO: Detect trivial changes and don't clear everything
        walletCopy.clearNodes();
        wallet = walletCopy.copy();
        save();
        EventManager.get().post(new WalletSettingsChangedEvent(wallet, getWalletFile()));
        refreshHistory(wallet.getStoredBlockHeight());
    }
}
