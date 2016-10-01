package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.Identity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GetUpdateChainTest {

    Activity mActivity = new Activity() {
        @Override
        public void taskJoin() {

        }

        @Override
        public void taskFail(int error) {

        }
    };

    @Test
    public void keyPairIsCorrectlyConvertedToString() {

        String id = "Yce8abgRANx8DQtctQ1YOAEjgDFP+PFtgZVAuuCogyE=";
        String host = "10.151.15.192";
        String port = "2003";

        Cothority cothority = new Cothority(host, port);

        Identity identity = new Identity(cothority, new byte[]{});
        identity.setStringId(id);

        new GetUpdateChain(mActivity, identity, true);
    }

}
