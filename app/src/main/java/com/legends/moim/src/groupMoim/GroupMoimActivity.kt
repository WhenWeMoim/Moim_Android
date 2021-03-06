package com.legends.moim.src.groupMoim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.legends.moim.R
import com.legends.moim.config.BaseActivity
import com.legends.moim.config.baseModel.Moim
import com.legends.moim.databinding.ActivityMoimGroupBinding
import com.legends.moim.src.groupMoim.dialog.InviteDialog
import com.legends.moim.src.groupMoim.dialog.ParticipantsDialog
import com.legends.moim.src.groupMoim.model.UserSchedules
import com.legends.moim.src.groupMoim.model.mySchedule
import com.legends.moim.src.groupMoim.model.thisMoim
import com.legends.moim.utils.FLAG_ACTIVITY_MAIN
import com.legends.moim.utils.FLAG_ACTIVITY_MAKEMOIM
import com.legends.moim.utils.FLAG_ACTIVITY_VIEWMOIM
import com.legends.moim.utils.getNickname


class GroupMoimActivity : BaseActivity() {

    private lateinit var binding : ActivityMoimGroupBinding
    private lateinit var groupScheduleFragment: GroupScheduleFragment
    private lateinit var transaction: FragmentTransaction
    private lateinit var fragmentManager: FragmentManager

    private val gson = Gson()

    private var userSchedules: Array<UserSchedules>? = null
    private var numOfParticipants = 1

    private val myName = getNickname()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setInitialize()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if( userSchedules == null ) {
            userSchedules = arrayOf( UserSchedules( userName = myName!!, schedules = mySchedule) )
        } else {
            for( i in userSchedules!!.indices ) {
                if( userSchedules!![i].userName == myName )
                    userSchedules!![i].schedules = mySchedule
            }
        }
        setGroupScheduleFragment()
    }

    private fun initView() {
        binding.moimGroupMoimNameTv.text = thisMoim.moimTitle
        binding.moimGroupMoimExplainTv.text = thisMoim.moimDescription

        binding.moimGroupParticipantTv.text = String.format("%d??? ??????", numOfParticipants)
    }

    private fun setInitialize() {
        binding = ActivityMoimGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when( intent.getIntExtra("startActivityFlag", -1) ) {
            FLAG_ACTIVITY_MAIN, FLAG_ACTIVITY_VIEWMOIM -> { //main?????? ?????? : ?????? ????????? ?????? & View moim?????? ?????? : ?????? ??????
                //getMoimInfoFromServer()
                thisMoim = getMoimInfo()

                if (!(intent.getStringExtra("moimSchedule").isNullOrBlank())) {
                    userSchedules = gson.fromJson(intent.getStringExtra("moimSchedule"), Array<UserSchedules>::class.java)

                    for(i in userSchedules!!.indices) {
                        if( userSchedules!![i].userName == myName )
                            mySchedule = userSchedules!![i].schedules
                    }

                    numOfParticipants = userSchedules!!.size
                }
                setGroupScheduleFragment()
            }
            FLAG_ACTIVITY_MAKEMOIM -> { // make moim?????? ?????? : ?????? ??????
                thisMoim = getMoimInfo()
                Log.d("MoimGroupActivity", "thisMoim Info : $thisMoim" )
                mySchedule = null
                setGroupScheduleFragment()
            }
            else -> {
                showDialog("?????? ??????", "?????? ????????? ????????? ????????? ??????????????????.\n?????? ????????? ?????????.", "??????")
                finish()
            }
        }

        //binding.moimGroupHomeBtn.setOnClickListener(this)
        binding.moimGroupInviteBtn.setOnClickListener(this)
        binding.moimGroupParticipantTv.setOnClickListener(this)
        binding.moimGroupAddPersonalBtn.setOnClickListener(this)
    }

    private fun setGroupScheduleFragment() {
        groupScheduleFragment = GroupScheduleFragment(binding, thisMoim, userSchedules)

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()
        transaction
            .replace(R.id.moim_group_schedule_fragment, groupScheduleFragment)
            .commitAllowingStateLoss()
    }

    private fun getMoimInfo(): Moim {
        if (intent.getStringExtra("moimInfo").isNullOrBlank()) {
            Toast.makeText(this, "?????? ?????? ??????. ?????? ??????????????????.", Toast.LENGTH_LONG).show()
            finish()
        }
        return gson.fromJson(intent.getStringExtra("moimInfo"), Moim::class.java)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id) {
            /*
            R.id.moim_group_home_btn -> { //????????? ????????????. ?????? ????????? ?????? ??? ??????(?????????)?????? ??????.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            */
            R.id.moim_group_invite_btn -> { //????????? ??????. ???????????? ????????? ?????????.
                showInviteDialog()
            }
            R.id.moim_group_participant_tv -> { //???????????? ??????, ~??? ??????, ????????? ????????? ????????? ????????????.
                val dig = ParticipantsDialog(this)
                if( userSchedules == null )
                    dig.showTimeDialog( arrayOf( UserSchedules( userName = myName!!, "") ) )
                else
                    dig.showTimeDialog(userSchedules)

            }
            R.id.moim_group_add_personal_btn -> { //?????? ????????? ??????
                val intent = Intent(this, PersonalMoimActivity::class.java)
                intent.putExtra("mySchedule", mySchedule)
                startActivity(intent)
            }
            //?????? 1, 2??? ????????? ?????? ????????? ???????????? fragment
            //?????? ???????????? - ????????? ???????????? ?????? ????????? ???????????? ??????
        }
    }

    private fun showInviteDialog() {
        if( thisMoim.password != null) {
            val dig = InviteDialog(this, thisMoim.moimIdx, thisMoim.password!!)
            dig.showInviteDialog()
        }
        else {
            val dig = InviteDialog(this, thisMoim.moimIdx, null)
            dig.showInviteDialog()
        }
    }
}