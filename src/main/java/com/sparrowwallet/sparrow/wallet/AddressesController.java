package com.sparrowwallet.sparrow.wallet;

import com.google.common.eventbus.Subscribe;
import com.sparrowwallet.drongo.KeyPurpose;
import com.sparrowwallet.drongo.wallet.WalletNode;
import com.sparrowwallet.sparrow.EventManager;
import com.sparrowwallet.sparrow.control.AddressTreeTable;
import com.sparrowwallet.sparrow.event.WalletHistoryChangedEvent;
import com.sparrowwallet.sparrow.event.WalletNodesChangedEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddressesController extends WalletFormController implements Initializable {
    @FXML
    private AddressTreeTable receiveTable;

    @FXML
    private AddressTreeTable changeTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventManager.get().register(this);
    }

    @Override
    public void initializeView() {
        receiveTable.initialize(getWalletForm().getNodeEntry(KeyPurpose.RECEIVE));
        changeTable.initialize(getWalletForm().getNodeEntry(KeyPurpose.CHANGE));
    }

    @Subscribe
    public void walletNodesChanged(WalletNodesChangedEvent event) {
        if(event.getWallet().equals(walletForm.getWallet())) {
            receiveTable.updateAll(getWalletForm().getNodeEntry(KeyPurpose.RECEIVE));
            changeTable.updateAll(getWalletForm().getNodeEntry(KeyPurpose.CHANGE));
        }
    }

    @Subscribe
    public void walletHistoryChanged(WalletHistoryChangedEvent event) {
        if(event.getWallet().equals(walletForm.getWallet())) {
            List<WalletNode> receiveNodes = event.getReceiveNodes();
            if(!receiveNodes.isEmpty()) {
                receiveTable.updateHistory(receiveNodes);
            }

            List<WalletNode> changeNodes = event.getChangeNodes();
            if(!changeNodes.isEmpty()) {
                changeTable.updateHistory(changeNodes);
            }
        }
    }
}
