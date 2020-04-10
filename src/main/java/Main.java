import com.ib.client.*;

import java.io.IOException;
import java.util.Vector;

public class Main {
    public static void main (String[] args) {
        Wrapper wrapper = new Wrapper();
        EReader reader;
        EClientSocket client = wrapper.getClient();
        EReaderSignal signal = wrapper.getReader();

        client.eConnect(null, 4002, 2);
        reader = new EReader(client, signal);
        reader.start();
        new Thread(() -> {
            while (client.isConnected()) {
                signal.waitForSignal();
                try { reader.processMsgs(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }).start();


        try { while (!client.isConnected() && wrapper.getOrderId() <= 0); } catch (Exception e) {}

        client.reqAccountSummary(4002, "All", "$LEDGER:USD");

//        Contract contract = new Contract();
//        contract.symbol("EUR");
//        contract.exchange("IDEALPRO");
//        contract.secType("CASH");
//        contract.currency("USD");
//
//        Vector<TagValue> mktDataOptions = new Vector<TagValue>();
//
//        client.reqMktData(0, contract, null, false, false, mktDataOptions);
    }
}
