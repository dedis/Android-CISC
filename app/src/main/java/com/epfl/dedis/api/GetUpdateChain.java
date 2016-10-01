package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.epfl.dedis.net.UpdateChain;
import com.google.gson.annotations.SerializedName;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class GetUpdateChain implements Request {

    private Activity mActivity;
    private Identity mIdentity;

    private class GetUpdateChainMessage {
        @SerializedName("LatestID")
        private String latestId;
    }

    public GetUpdateChain(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public GetUpdateChain(Activity activity, Identity identity, boolean wait) {
        mActivity = activity;
        mIdentity = identity;

        GetUpdateChainMessage getUpdateChainMessage = new GetUpdateChainMessage();
        getUpdateChainMessage.latestId = identity.getStringId();

        HTTP http = new HTTP(this, identity.getCothority(), GET_UPDATE_CHAIN, Utils.toJson(getUpdateChainMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public byte[] hash(UpdateChain uc) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(uc.getChain()[0].getFix().mIndex);
        sha256.update(buffer.array());

        buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(uc.getChain()[0].getFix().mHeight);
        sha256.update(buffer.array());

        buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(uc.getChain()[0].getFix().mMaximumHeight);
        sha256.update(buffer.array());

        buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(uc.getChain()[0].getFix().mBaseHeight);
        sha256.update(buffer.array());

        for (String s : uc.getChain()[0].getFix().mBackLinkIds) {
            sha256.update(Utils.decodeBase64(s));
        }

        sha256.update(Utils.decodeBase64(uc.getChain()[0].getFix().mVerifierId));
        sha256.update(Utils.decodeBase64(uc.getChain()[0].getFix().mParentBlockId));
        sha256.update(Utils.decodeBase64(uc.getChain()[0].getFix().mAggregate));
        sha256.update(Utils.decodeBase64(uc.getChain()[0].getFix().mAggregateResp));
        sha256.update(Utils.decodeBase64(uc.getChain()[0].getFix().mData));

        byte[] hash = sha256.digest();
        System.out.println("NEW HASH: " + Arrays.toString(hash));

        return hash;
    }

    public void callback(String result) {
        UpdateChain uc = Utils.fromJson(result, UpdateChain.class);

        System.out.println("OLD HASH: " + Arrays.toString(Utils.decodeBase64(uc.getChain()[0].getId())));
        try {
            //hash(uc);
            byte[] a = hash(uc);
            System.out.println(Arrays.toString(a));
            if (Arrays.equals(Utils.decodeBase64(uc.getChain()[0].getId()), a))
            {
                System.out.println("HASH IS EQUAL!");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // TODO: More or more detailed error messages; also for other Actitivies
    // TODO: Check if HTTP-error codes are suitable
    public void callbackError(int error) {
        switch (error) {
            case 400: mActivity.taskFail(R.string.err_400); break;
            case 500: mActivity.taskFail(R.string.err_500); break;
            case 501: mActivity.taskFail(R.string.err_501); break;
            case 502: mActivity.taskFail(R.string.err_502); break;
            case 503: mActivity.taskFail(R.string.err_503); break;
            case 504: mActivity.taskFail(R.string.err_504); break;
            default: mActivity.taskFail(R.string.err_unknown);
        }
    }

}
