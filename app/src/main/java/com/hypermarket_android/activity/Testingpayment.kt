package com.hypermarket_android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hypermarket_android.R
import com.hypermarket_android.paymentgateway.PaymentActivity
import mumbai.dev.sdkdubai.*
import net.cachapa.expandablelayout.ExpandableLayout
import java.text.SimpleDateFormat
import java.util.*

class Testingpayment : AppCompatActivity() , View.OnClickListener, CustomModel.OnCustomStateListener/*, responseinterface*/ {
    val exp_merchant_details: ExpandableLayout? = null
    var exp_billing_address:net.cachapa.expandablelayout.ExpandableLayout? = null
    var exp_shipping_address:net.cachapa.expandablelayout.ExpandableLayout? = null
    var exp_standard_instructions:net.cachapa.expandablelayout.ExpandableLayout? = null

    //    ExpandableLayout exp_merchant_details, exp_billing_address, exp_shipping_address, exp_standard_instructions;

    var myCalendar = Calendar.getInstance()
    var myFormat = "dd-MM-yyyy" //In which you need put here

    var sdf = SimpleDateFormat(myFormat, Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testingpayment)
        CustomModel.getInstance().setListener(this)
        getCardPayment()
    }

    fun getCardPayment() {
        val randomNum = PaymentActivity.randInt(0, 9999999)
        val m = MerchantDetails()
        m.access_code = "AVQN03IK92BL47NQLB"
        m.merchant_id = "45990"
        m.currency = "AED"
//        m.amount = totalAmount
        m.amount = "1.00"
        m.redirect_url = "https://yasmart.azurewebsites.net/ccavResponseHandler"
        m.cancel_url = "https://yasmart.azurewebsites.net/ccavResponseHandler"
        m.rsa_url = "https://yasmart.azurewebsites.net/getrsa"
        m.order_id = randomNum.toString()
//        m.customer_id = sharedPreference.storeId.toString()
        m.customer_id = "ABC"
        m.promo_code = ""
        m.add1 = "test1"
        m.add2 = "test1"
        m.add3 = "test1"
        m.add4 = "test1"
        m.add5 = "test1"
        val b = BillingAddress()
        b.name = "ABC"
        b.address = "Star City"
        b.country = "United Arab Emirates"
        b.state = "Dubai"
        b.city = "Dubai"
        b.telephone = "+9711234567890"
        b.email = "test@gmail.com"
        val s = ShippingAddress()
        s.name = "XYZ"
        s.address = "Internet City"
        s.country = "United Arab Emirates"
        s.state = "Dubai"
        s.city = "Dubai"
        s.telephone = "+9719876543210"
        m.isCCAvenue_promo = false

        m.isShow_addr = false


        /*// SI data //
            StandardInstructions si = new StandardInstructions();

            String setup_amt ="";
            int selected_setup_amt = radioGroup_setup_amt.getCheckedRadioButtonId();
            if (selected_setup_amt == R.id.radio_yes){
                setup_amt = "Y";
            }else if (selected_setup_amt == R.id.radio_no) {
                setup_amt = "N";
            }

            String freq_type = "";
            int selected_freq_type = radioGroup_freq_type.getCheckedRadioButtonId();
            if (selected_freq_type == R.id.radio_days){
                freq_type = "days";
            }else if (selected_freq_type == R.id.radio_month){
                freq_type = "month";
            }else if (selected_freq_type == R.id.radio_year){



                freq_type = "year";
            }

            String si_type = "";
            int selected_si_type = radioGroup_si_type.getCheckedRadioButtonId();
            if (selected_si_type == R.id.radio_fixed){
                si_type = "FIXED";
                if (!setup_amt.equals("") && !ed_si_amount.getText().toString().equals("")
                        && !start_date.getText().toString().equals("") && !ed_si_freq.getText().toString().equals("")
                        && !ed_bill_cycle.getText().toString().equals("") && !freq_type.equals("")){

                    si.setSi_type(si_type);
                    si.setSi_mer_ref_no(ed_si_ref_no.getText().toString());
                    si.setSi_is_setup_amt(setup_amt);
                    si.setSi_amount(ed_si_amount.getText().toString());
                    si.setSi_start_date(start_date.getText().toString());
                    si.setSi_frequency_type(freq_type);
                    si.setSi_frequency(ed_si_freq.getText().toString());
                    si.setSi_bill_cycle(ed_bill_cycle.getText().toString());

                    Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    //Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    // Intent i =new Intent(MainActivity.this,PaymentDetails.class);
                    i.putExtra("merchant",m);
                    i.putExtra("billing",b);
                    i.putExtra("shipping",s);
                    i.putExtra("standard instructions", si);
                    startActivity(i);

                }else {
                    Toast.makeText(this, "SI Parameters missing", Toast.LENGTH_SHORT).show();
                }

            }else if (selected_si_type == R.id.radio_on_demand){
                si_type = "ONDEMAND";
                if (!start_date.getText().toString().equals("") && !setup_amt.equals("")){

                    si.setSi_type(si_type);
                    si.setSi_mer_ref_no(ed_si_ref_no.getText().toString());
                    si.setSi_is_setup_amt(setup_amt);
                    si.setSi_start_date(start_date.getText().toString());

                    Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    //Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                    // Intent i =new Intent(MainActivity.this,PaymentDetails.class);
                    i.putExtra("merchant",m);
                    i.putExtra("billing",b);
                    i.putExtra("shipping",s);
                    i.putExtra("standard instructions", si);
                    startActivity(i);

                }else {
                    Toast.makeText(this, "SI Parameters missing", Toast.LENGTH_SHORT).show();
                }
            }else {

                si.setSi_type("");

                Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                //Intent i =new Intent(MainActivity.this,PaymentOptions.class);
                // Intent i =new Intent(MainActivity.this,PaymentDetails.class);
                i.putExtra("merchant",m);
                i.putExtra("billing",b);
                i.putExtra("shipping",s);
                i.putExtra("standard instructions", si);
                startActivity(i);
            }*/
        val i = Intent(this@Testingpayment, PaymentOptions::class.java)
        i.putExtra("merchant", m)
        i.putExtra("billing", b)
        i.putExtra("shipping", s)
        startActivity(i)
    }


    override fun onClick(p0: View?) {

    }

    override fun stateChanged() {
        val modelState = CustomModel.getInstance().state

        Log.e("stateChanged", modelState.toString())
    }
}