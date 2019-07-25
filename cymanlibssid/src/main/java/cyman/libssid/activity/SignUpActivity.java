package cyman.libssid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.CaptureResult;
import android.os.Bundle;

import java.io.ByteArrayInputStream;

import javax.crypto.Cipher;

import cyman.libssid.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        enrollStmt.setBinaryStream(1,new ByteArrayInputStream(template.getData()), template.getData().length);

    }


//    private void OnCaptured(CaptureResult captureResult)
//    {
//        try
//        {
//            // Check capture quality and throw an error if bad.
//            if (!_sender.CheckCaptureResult(captureResult)) return;
//            count++;
//            DataResult<Fmd> resultConversion = FeatureExtraction.CreateFmdFromFid(captureResult.Data, Constants.Formats.Fmd.ANSI);
//            SendMessage(Action.SendMessage, "A finger was captured.  \r\nCount:  " + (count));
//            if (resultConversion.ResultCode != Constants.ResultCode.DP_SUCCESS)
//            {
//                _sender.Reset = true;
//                throw new Exception(resultConversion.ResultCode.ToString());
//            }
//            preenrollmentFmds.Add(resultConversion.Data);
//            if (count >= 4)
//            {
//                DataResult<Fmd> resultEnrollment = DPUruNet.Enrollment.CreateEnrollmentFmd(Constants.Formats.Fmd.ANSI, preenrollmentFmds);
//                if (resultEnrollment.ResultCode == Constants.ResultCode.DP_SUCCESS)
//                {
//                    SendMessage(Action.SendMessage, "An enrollment FMD was successfully created.");
//                    SendMessage(Action.SendMessage, "Place a finger on the reader.");
//                    preenrollmentFmds.Clear();
//                    count = 0;
//                    return;
//                }
//                else if (resultEnrollment.ResultCode == Constants.ResultCode.DP_ENROLLMENT_INVALID_SET)
//                {
//                    SendMessage(Action.SendMessage, "Enrollment was unsuccessful.  Please try again.");
//                    SendMessage(Action.SendMessage, "Place a finger on the reader.");
//                    preenrollmentFmds.Clear();
//                    count = 0;
//                    return;
//                }
//            }
//            SendMessage(Action.SendMessage, "Now place the same finger on the reader.");
//        }
//        catch (Exception ex)
//        {
//            // Send error message, then close form
//            SendMessage(Action.SendMessage, "Error:  " + ex.Message);
//        }
//    }
}
