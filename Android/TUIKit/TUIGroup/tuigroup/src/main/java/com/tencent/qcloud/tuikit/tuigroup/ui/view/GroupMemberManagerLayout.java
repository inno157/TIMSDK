package com.tencent.qcloud.tuikit.tuigroup.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tencent.qcloud.tuicore.component.TitleBarLayout;
import com.tencent.qcloud.tuicore.component.interfaces.ITitleBarLayout;
import com.tencent.qcloud.tuicore.util.PopWindowUtil;
import com.tencent.qcloud.tuikit.tuigroup.R;
import com.tencent.qcloud.tuikit.tuigroup.bean.GroupInfo;
import com.tencent.qcloud.tuikit.tuigroup.bean.GroupMemberInfo;
import com.tencent.qcloud.tuikit.tuigroup.component.BottomSelectSheet;
import com.tencent.qcloud.tuikit.tuigroup.ui.interfaces.IGroupMemberLayout;
import com.tencent.qcloud.tuikit.tuigroup.ui.interfaces.IGroupMemberChangedCallback;
import com.tencent.qcloud.tuikit.tuigroup.ui.interfaces.IGroupMemberRouter;
import com.tencent.qcloud.tuikit.tuigroup.presenter.GroupInfoPresenter;
import com.tencent.qcloud.tuikit.tuigroup.ui.page.GroupInfoFragment;

import java.util.ArrayList;
import java.util.List;


public class GroupMemberManagerLayout extends LinearLayout implements IGroupMemberLayout {

    private TitleBarLayout mTitleBar;
    private AlertDialog mDialog;
    private GroupMemberManagerAdapter mAdapter;
    private IGroupMemberRouter mGroupMemberManager;
    private GroupInfo mGroupInfo;

    private GroupInfoPresenter presenter;
    private GroupInfoFragment.GroupMembersListener mGroupMembersListener;

    public GroupMemberManagerLayout(Context context) {
        super(context);
        init();
    }

    public GroupMemberManagerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupMemberManagerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.group_member_layout, this);
        mTitleBar = findViewById(R.id.group_member_title_bar);
        mTitleBar.setTitle(getContext().getString(R.string.manager), ITitleBarLayout.Position.RIGHT);
        mTitleBar.getRightIcon().setVisibility(GONE);
        mTitleBar.setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buildPopMenu();
            }
        });
        mAdapter = new GroupMemberManagerAdapter();
        mAdapter.setMemberChangedCallback(new IGroupMemberChangedCallback() {

            @Override
            public void onMemberRemoved(GroupMemberInfo memberInfo) {
                mTitleBar.setTitle(getContext().getString(R.string.group_members) +
                        "(" + mGroupInfo.getMemberCount() + ")", TitleBarLayout.Position.MIDDLE);
            }
        });
        GridView gridView = findViewById(R.id.group_all_members);
        gridView.setAdapter(mAdapter);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (mGroupMembersListener != null && mGroupInfo!= null && mGroupInfo.getNextSeq() != 0) {
                                mGroupMembersListener.loadMoreGroupMember(mGroupInfo);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    public void onGroupInfoChanged(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
        presenter.setGroupInfo(groupInfo);
        mAdapter.setDataSource(groupInfo);
        if (groupInfo != null) {
            mTitleBar.setTitle(getContext().getString(R.string.group_members) +
                    "(" + groupInfo.getMemberCount() + ")", TitleBarLayout.Position.MIDDLE);
        }
    }

    public void setGroupMembersListener(GroupInfoFragment.GroupMembersListener listener) {
        mGroupMembersListener = listener;
    }

    @Override
    public void onGroupInfoModified(Object value, int type) {

    }

    private void buildPopMenu() {
        if (mGroupInfo == null) {
            return;
        }

        BottomSelectSheet sheet = new BottomSelectSheet(getContext());
        List<String> stringList = new ArrayList<>();
        String addMember = getResources().getString(R.string.add_group_member);
        String removeMember = getResources().getString(R.string.remove_group_member);
        stringList.add(addMember);
        if (mGroupInfo.isOwner()) {
            stringList.add(removeMember);
        }
        sheet.setSelectList(stringList);
        sheet.setOnClickListener(new BottomSelectSheet.BottomSelectSheetOnClickListener() {
            @Override
            public void onSheetClick(int index) {
                if (index == 0) {
                    if (mGroupMemberManager != null) {
                        mGroupMemberManager.forwardAddMember(mGroupInfo);
                    }
                } else if (index == 1) {
                    if (mGroupMemberManager != null) {
                        mGroupMemberManager.forwardDeleteMember(mGroupInfo);
                    }
                }
            }
        });
        sheet.show();
    }

    public void setRouter(IGroupMemberRouter callBack) {
        this.mGroupMemberManager = callBack;
    }

    public void setPresenter(GroupInfoPresenter presenter) {
        this.presenter = presenter;
        mAdapter.setPresenter(presenter);
    }
}
