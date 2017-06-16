package com.lyy_wzw.comeacross.rong.server;

import android.content.Context;
import android.text.TextUtils;

import com.lyy_wzw.comeacross.rong.server.network.http.HttpException;
import com.lyy_wzw.comeacross.rong.server.request.AddGroupMemberRequest;
import com.lyy_wzw.comeacross.rong.server.request.AddToBlackListRequest;
import com.lyy_wzw.comeacross.rong.server.request.AgreeFriendsRequest;
import com.lyy_wzw.comeacross.rong.server.request.ChangePasswordRequest;
import com.lyy_wzw.comeacross.rong.server.request.CheckPhoneRequest;
import com.lyy_wzw.comeacross.rong.server.request.CreateGroupRequest;
import com.lyy_wzw.comeacross.rong.server.request.DeleteFriendRequest;
import com.lyy_wzw.comeacross.rong.server.request.DeleteGroupMemberRequest;
import com.lyy_wzw.comeacross.rong.server.request.DismissGroupRequest;
import com.lyy_wzw.comeacross.rong.server.request.FriendInvitationRequest;
import com.lyy_wzw.comeacross.rong.server.request.JoinGroupRequest;
import com.lyy_wzw.comeacross.rong.server.request.QuitGroupRequest;
import com.lyy_wzw.comeacross.rong.server.request.RegisterRequest;
import com.lyy_wzw.comeacross.rong.server.request.RemoveFromBlacklistRequest;
import com.lyy_wzw.comeacross.rong.server.request.RestPasswordRequest;
import com.lyy_wzw.comeacross.rong.server.request.SetFriendDisplayNameRequest;
import com.lyy_wzw.comeacross.rong.server.request.SetGroupDisplayNameRequest;
import com.lyy_wzw.comeacross.rong.server.request.SetGroupNameRequest;
import com.lyy_wzw.comeacross.rong.server.request.SetGroupPortraitRequest;
import com.lyy_wzw.comeacross.rong.server.request.SetNameRequest;
import com.lyy_wzw.comeacross.rong.server.request.SetPortraitRequest;
import com.lyy_wzw.comeacross.rong.server.request.VerifyCodeRequest;
import com.lyy_wzw.comeacross.rong.server.response.AddGroupMemberResponse;
import com.lyy_wzw.comeacross.rong.server.response.AddToBlackListResponse;
import com.lyy_wzw.comeacross.rong.server.response.AgreeFriendsResponse;
import com.lyy_wzw.comeacross.rong.server.response.ChangePasswordResponse;
import com.lyy_wzw.comeacross.rong.server.response.CheckPhoneResponse;
import com.lyy_wzw.comeacross.rong.server.response.CreateGroupResponse;
import com.lyy_wzw.comeacross.rong.server.response.DefaultConversationResponse;
import com.lyy_wzw.comeacross.rong.server.response.DeleteFriendResponse;
import com.lyy_wzw.comeacross.rong.server.response.DeleteGroupMemberResponse;
import com.lyy_wzw.comeacross.rong.server.response.DismissGroupResponse;
import com.lyy_wzw.comeacross.rong.server.response.FriendInvitationResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetBlackListResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetFriendInfoByIDResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetGroupInfoResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetGroupMemberResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetGroupResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetTokenResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetUserInfoByIdResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetUserInfoByPhoneResponse;
import com.lyy_wzw.comeacross.rong.server.response.GetUserInfosResponse;
import com.lyy_wzw.comeacross.rong.server.response.JoinGroupResponse;
import com.lyy_wzw.comeacross.rong.server.response.QiNiuTokenResponse;
import com.lyy_wzw.comeacross.rong.server.response.QuitGroupResponse;
import com.lyy_wzw.comeacross.rong.server.response.RegisterResponse;
import com.lyy_wzw.comeacross.rong.server.response.RemoveFromBlackListResponse;
import com.lyy_wzw.comeacross.rong.server.response.RestPasswordResponse;
import com.lyy_wzw.comeacross.rong.server.response.SetFriendDisplayNameResponse;
import com.lyy_wzw.comeacross.rong.server.response.SetGroupDisplayNameResponse;
import com.lyy_wzw.comeacross.rong.server.response.SetGroupNameResponse;
import com.lyy_wzw.comeacross.rong.server.response.SetGroupPortraitResponse;
import com.lyy_wzw.comeacross.rong.server.response.SetNameResponse;
import com.lyy_wzw.comeacross.rong.server.response.SetPortraitResponse;
import com.lyy_wzw.comeacross.rong.server.response.SyncTotalDataResponse;
import com.lyy_wzw.comeacross.rong.server.response.UserRelationshipResponse;
import com.lyy_wzw.comeacross.rong.server.response.VerifyCodeResponse;
import com.lyy_wzw.comeacross.rong.server.response.VersionResponse;
import com.lyy_wzw.comeacross.rong.server.utils.NLog;
import com.lyy_wzw.comeacross.rong.server.utils.json.JsonMananger;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.lyy_wzw.comeacross.rong.server.utils.json.JsonMananger.jsonToBean;


/**
 * Created by 27459 on 2017/6/15.
 */

public class WeixinAction extends BaseAction {
    private final String CONTENT_TYPE = "application/json";
    private final String ENCODING = "utf-8";

    public WeixinAction(Context context) {
        super(context);
    }

    /**
     * 发送验证码
     *
     * @param region 国家码
     * @param phone 手机号
     * @return
     * @throws HttpException
     */
    public CheckPhoneResponse checkPhoneAvailabel(String region,String phone) throws HttpException{
        String url = getURL("user/check_phone_available");
        String json = JsonMananger.beanToJson(new CheckPhoneRequest(phone,region));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json,ENCODING);
            entity.setContentType(CONTENT_TYPE);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CheckPhoneResponse response = null;
        String result = httpManager.post(mContext,url,entity,CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)){
            response =jsonTobean(result,CheckPhoneResponse.class);
        }
        return response;
    }

        /*
    * 200: 验证成功
    1000: 验证码错误
    2000: 验证码过期
    异常返回，返回的 HTTP Status Code 如下：

    400: 错误的请求
    500: 应用服务器内部错误
    * */

    /**
     * 验证验证码是否正确
     * @param region 国家码
     * @param phone 手机号
     * @param code
     * @return
     * @throws HttpException
     */
    public VerifyCodeResponse verifyCode(String region,String phone,String code)throws HttpException{
        String url = getURL("user/verify_code");
        String json = JsonMananger.beanToJson(new VerifyCodeRequest(region,phone,code));
        VerifyCodeResponse response = null;
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext,url,entity,CONTENT_TYPE);
        if (TextUtils.isEmpty(result)) {
            response = jsonTobean(result,VerifyCodeResponse.class);
        }
        return response;
    }

    /**
     * 注册
     * @param nickName
     * @param password
     * @param verification_token
     * @return
     * @throws HttpException
     */
    public RegisterResponse register(String nickName,String password,String verification_token)throws  HttpException{
        String url = getURL("user/register");
        StringEntity entity = null;
        try {
            entity = new StringEntity(JsonMananger.beanToJson(new RegisterRequest(nickName,password,verification_token)),ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RegisterResponse response = null;
        String result = httpManager.post(mContext,url,entity,CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            response = jsonTobean(result,RegisterResponse.class);
        }
        return response;
    }

    /**
     * 获取token 前置条件 502 坏的网关 测试环境用户已达上限
     * @return
     * @throws HttpException
     */
    public GetTokenResponse getToken() throws HttpException{
        String url = getURL("user/get_token");
        String result = httpManager.get(url);
        GetTokenResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonTobean(result,GetTokenResponse.class);
        }
        return response;
    }

    public SetNameResponse setName(String nickname) throws HttpException{
        String url = getURL("user/set_nickname");
        String json = JsonMananger.beanToJson(new SetNameRequest(nickname));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SetNameResponse response = null;
        String result = httpManager.post(mContext,url,entity,CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            response = jsonTobean(result,SetNameResponse.class);
        }
        return response;
    }

    /**
     * 设置用户头像
     * @param portraitUri 头像 path
     * @return
     * @throws HttpException
     */
    public SetPortraitResponse setPortrait(String portraitUri)throws HttpException{
        String url = getURL("user/set_portrait_uri");
        String json = JsonMananger.beanToJson(new SetPortraitRequest(portraitUri));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json,ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SetPortraitResponse resonse = null;
        String result = httpManager.post(mContext,url,entity,CONTENT_TYPE);
        if (TextUtils.isEmpty(result)){
            resonse = jsonTobean(result,SetPortraitResponse.class);
        }

        return resonse;
    }

    /**
     * 当前登录用户通过旧密码设置新密码  前置条件需要登录才能访问
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @throws HttpException
     */
    public ChangePasswordResponse changePassword(String oldPassword, String newPassword) throws HttpException {
        String url = getURL("user/change_password");
        String json = JsonMananger.beanToJson(new ChangePasswordRequest(oldPassword, newPassword));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        ChangePasswordResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("ChangePasswordResponse", result);
            response = jsonToBean(result, ChangePasswordResponse.class);
        }
        return response;
    }


    /**
     * 通过手机验证码重置密码
     *
     * @param password           密码，6 到 20 个字节，不能包含空格
     * @param verification_token 调用 /user/verify_code 成功后返回的 activation_token
     * @throws HttpException
     */
    public RestPasswordResponse restPassword(String password, String verification_token) throws HttpException {
        String uri = getURL("user/reset_password");
        String json = JsonMananger.beanToJson(new RestPasswordRequest(password, verification_token));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, uri, entity, CONTENT_TYPE);
        RestPasswordResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("RestPasswordResponse", result);
            response = jsonToBean(result, RestPasswordResponse.class);
        }
        return response;
    }

    /**
     * 根据 id 去服务端查询用户信息
     *
     * @param userid 用户ID
     * @throws HttpException
     */
    public GetUserInfoByIdResponse getUserInfoById(String userid) throws HttpException {
        String url = getURL("user/" + userid);
        String result = httpManager.get(url);
        GetUserInfoByIdResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetUserInfoByIdResponse.class);
        }
        return response;
    }


    /**
     * 通过国家码和手机号查询用户信息
     *
     * @param region 国家码
     * @param phone  手机号
     * @throws HttpException
     */
    public GetUserInfoByPhoneResponse getUserInfoFromPhone(String region, String phone) throws HttpException {
        String url = getURL("user/find/" + region + "/" + phone);
        String result = httpManager.get(url);
        GetUserInfoByPhoneResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetUserInfoByPhoneResponse.class);
        }
        return response;
    }


    /**
     * 发送好友邀请
     *
     * @param userid           好友id
     * @param addFriendMessage 添加好友的信息
     * @throws HttpException
     */
    public FriendInvitationResponse sendFriendInvitation(String userid, String addFriendMessage) throws HttpException {
        String url = getURL("friendship/invite");
        String json = JsonMananger.beanToJson(new FriendInvitationRequest(userid, addFriendMessage));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        FriendInvitationResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, FriendInvitationResponse.class);
        }
        return response;
    }


    /**
     * 获取发生过用户关系的列表
     *
     * @throws HttpException
     */
    public UserRelationshipResponse getAllUserRelationship() throws HttpException {
        String url = getURL("friendship/all");
        String result = httpManager.get(url);
        UserRelationshipResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, UserRelationshipResponse.class);
        }
        return response;
    }

    /**
     * 根据userId去服务器查询好友信息
     *
     * @throws HttpException
     */
    public GetFriendInfoByIDResponse getFriendInfoByID(String userid) throws HttpException {
        String url = getURL("friendship/" + userid + "/profile");
        String result = httpManager.get(url);
        GetFriendInfoByIDResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetFriendInfoByIDResponse.class);
        }
        return response;
    }

    /**
     * 同意对方好友邀请
     *
     * @param friendId 好友ID
     * @throws HttpException
     */
    public AgreeFriendsResponse agreeFriends(String friendId) throws HttpException {
        String url = getURL("friendship/agree");
        String json = JsonMananger.beanToJson(new AgreeFriendsRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AgreeFriendsResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AgreeFriendsResponse.class);
        }
        return response;
    }

    /**
     * 创建群组
     *
     * @param name      群组名
     * @param memberIds 群组成员id
     * @throws HttpException
     */
    public CreateGroupResponse createGroup(String name, List<String> memberIds) throws HttpException {
        String url = getURL("group/create");
        String json = JsonMananger.beanToJson(new CreateGroupRequest(name, memberIds));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        CreateGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, CreateGroupResponse.class);
        }
        return response;
    }

    /**
     * 创建者设置群组头像
     *
     * @param groupId     群组Id
     * @param portraitUri 群组头像
     * @throws HttpException
     */
    public SetGroupPortraitResponse setGroupPortrait(String groupId, String portraitUri) throws HttpException {
        String url = getURL("group/set_portrait_uri");
        String json = JsonMananger.beanToJson(new SetGroupPortraitRequest(groupId, portraitUri));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetGroupPortraitResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetGroupPortraitResponse.class);
        }
        return response;
    }

    /**
     * 获取当前用户所属群组列表
     *
     * @throws HttpException
     */
    public GetGroupResponse getGroups() throws HttpException {
        String url = getURL("user/groups");
        String result = httpManager.get(mContext, url);
        GetGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetGroupResponse.class);
        }
        return response;
    }

    /**
     * 根据 群组id 查询该群组信息   403 群组成员才能看
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public GetGroupInfoResponse getGroupInfo(String groupId) throws HttpException {
        String url = getURL("group/" + groupId);
        String result = httpManager.get(mContext, url);
        GetGroupInfoResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetGroupInfoResponse.class);
        }
        return response;
    }

    /**
     * 根据群id获取群组成员
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public GetGroupMemberResponse getGroupMember(String groupId) throws HttpException {
        String url = getURL("group/" + groupId + "/members");
        String result = httpManager.get(mContext, url);
        GetGroupMemberResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetGroupMemberResponse.class);
        }
        return response;
    }

    /**
     * 当前用户添加群组成员
     *
     * @param groupId   群组Id
     * @param memberIds 成员集合
     * @throws HttpException
     */
    public AddGroupMemberResponse addGroupMember(String groupId, List<String> memberIds) throws HttpException {
        String url = getURL("group/add");
        String json = JsonMananger.beanToJson(new AddGroupMemberRequest(groupId, memberIds));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AddGroupMemberResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AddGroupMemberResponse.class);
        }
        return response;
    }

    /**
     * 创建者将群组成员提出群组
     *
     * @param groupId   群组Id
     * @param memberIds 成员集合
     * @throws HttpException
     */
    public DeleteGroupMemberResponse deleGroupMember(String groupId, List<String> memberIds) throws HttpException {
        String url = getURL("group/kick");
        String json = JsonMananger.beanToJson(new DeleteGroupMemberRequest(groupId, memberIds));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        DeleteGroupMemberResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DeleteGroupMemberResponse.class);
        }
        return response;
    }

    /**
     * 创建者更改群组昵称
     *
     * @param groupId 群组Id
     * @param name    群昵称
     * @throws HttpException
     */
    public SetGroupNameResponse setGroupName(String groupId, String name) throws HttpException {
        String url = getURL("group/rename");
        String json = JsonMananger.beanToJson(new SetGroupNameRequest(groupId, name));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetGroupNameResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetGroupNameResponse.class);
        }
        return response;
    }

    /**
     * 用户自行退出群组
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public QuitGroupResponse quitGroup(String groupId) throws HttpException {
        String url = getURL("group/quit");
        String json = JsonMananger.beanToJson(new QuitGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        QuitGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, QuitGroupResponse.class);
        }
        return response;
    }

    /**
     * 创建者解散群组
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public DismissGroupResponse dissmissGroup(String groupId) throws HttpException {
        String url = getURL("group/dismiss");
        String json = JsonMananger.beanToJson(new DismissGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        DismissGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DismissGroupResponse.class);
        }
        return response;
    }


    /**
     * 修改自己的当前的群昵称
     *
     * @param groupId     群组Id
     * @param displayName 群名片
     * @throws HttpException
     */
    public SetGroupDisplayNameResponse setGroupDisplayName(String groupId, String displayName) throws HttpException {
        String url = getURL("group/set_display_name");
        String json = JsonMananger.beanToJson(new SetGroupDisplayNameRequest(groupId, displayName));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetGroupDisplayNameResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetGroupDisplayNameResponse.class);
        }
        return response;
    }

    /**
     * 删除好友
     *
     * @param friendId 好友Id
     * @throws HttpException
     */
    public DeleteFriendResponse deleteFriend(String friendId) throws HttpException {
        String url = getURL("friendship/delete");
        String json = JsonMananger.beanToJson(new DeleteFriendRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        DeleteFriendResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DeleteFriendResponse.class);
        }
        return response;
    }

    /**
     * 设置好友的备注名称
     *
     * @param friendId    好友Id
     * @param displayName 备注名
     * @throws HttpException
     */
    public SetFriendDisplayNameResponse setFriendDisplayName(String friendId, String displayName) throws HttpException {
        String url = getURL("friendship/set_display_name");
        String json = JsonMananger.beanToJson(new SetFriendDisplayNameRequest(friendId, displayName));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetFriendDisplayNameResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetFriendDisplayNameResponse.class);
        }
        return response;
    }

    /**
     * 获取黑名单
     *
     * @throws HttpException
     */
    public GetBlackListResponse getBlackList() throws HttpException {
        String url = getURL("user/blacklist");
        String result = httpManager.get(mContext, url);
        GetBlackListResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetBlackListResponse.class);
        }
        return response;
    }

    /**
     * 加入黑名单
     *
     * @param friendId 群组Id
     * @throws HttpException
     */
    public AddToBlackListResponse addToBlackList(String friendId) throws HttpException {
        String url = getURL("user/add_to_blacklist");
        String json = JsonMananger.beanToJson(new AddToBlackListRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AddToBlackListResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AddToBlackListResponse.class);
        }
        return response;
    }

    /**
     * 移除黑名单
     *
     * @param friendId 好友Id
     * @throws HttpException
     */
    public RemoveFromBlackListResponse removeFromBlackList(String friendId) throws HttpException {
        String url = getURL("user/remove_from_blacklist");
        String json = JsonMananger.beanToJson(new RemoveFromBlacklistRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        RemoveFromBlackListResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, RemoveFromBlackListResponse.class);
        }
        return response;
    }

    public QiNiuTokenResponse getQiNiuToken() throws HttpException {
        String url = getURL("user/get_image_token");
        String result = httpManager.get(mContext, url);
        QiNiuTokenResponse q = null;
        if (!TextUtils.isEmpty(result)) {
            q = jsonToBean(result, QiNiuTokenResponse.class);
        }
        return q;
    }


    /**
     * 当前用户加入某群组
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public JoinGroupResponse JoinGroup(String groupId) throws HttpException {
        String url = getURL("group/join");
        String json = JsonMananger.beanToJson(new JoinGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        JoinGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, JoinGroupResponse.class);
        }
        return response;
    }


    /**
     * 获取默认群组 和 聊天室
     *
     * @throws HttpException
     */
    public DefaultConversationResponse getDefaultConversation() throws HttpException {
        String url = getURL("misc/demo_square");
        String result = httpManager.get(mContext, url);
        DefaultConversationResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DefaultConversationResponse.class);
        }
        return response;
    }

    /**
     * 根据一组ids 获取 一组用户信息
     *
     * @param ids 用户 id 集合
     * @throws HttpException
     */
    public GetUserInfosResponse getUserInfos(List<String> ids) throws HttpException {
        String url = getURL("user/batch?");
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append("id=");
            sb.append(s);
            sb.append("&");
        }
        String stringRequest = sb.substring(0, sb.length() - 1);
        String newUrl = url + stringRequest;
        String result = httpManager.get(mContext, newUrl);
        GetUserInfosResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetUserInfosResponse.class);
        }
        return response;
    }

    /**
     * 获取版本信息
     *
     * @throws HttpException
     */
    public VersionResponse getSealTalkVersion() throws HttpException {
        String url = getURL("misc/client_version");
        String result = httpManager.get(mContext, url.trim());
        VersionResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, VersionResponse.class);
        }
        return response;
    }

    public SyncTotalDataResponse syncTotalData(String version) throws HttpException {
        String url = getURL("user/sync/" + version);
        String result = httpManager.get(mContext, url);
        SyncTotalDataResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SyncTotalDataResponse.class);
        }
        return response;
    }

//    /**
//     * 根据userId去服务器查询好友信息
//     *
//     * @throws HttpException
//     */
//    public GetFriendInfoByIDResponse getFriendInfoByID(String userid) throws HttpException {
//        String url = getURL("friendship/" + userid + "/profile");
//        String result = httpManager.get(url);
//        GetFriendInfoByIDResponse response = null;
//        if (!TextUtils.isEmpty(result)) {
//            response = jsonToBean(result, GetFriendInfoByIDResponse.class);
//        }
//        return response;
//    }
    /**
     //     * 根据userId去服务器查询好友信息
     //     *
     //     * @throws HttpException
     //     */





}
