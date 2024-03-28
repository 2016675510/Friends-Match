package com.friends.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.friends.common.BaseResponse;
import com.friends.common.DeleteRequest;
import com.friends.common.ErrorCode;
import com.friends.common.ResultUtils;
import com.friends.exception.BusinessException;
import com.friends.model.domain.Team;
import com.friends.model.domain.User;
import com.friends.model.domain.UserTeam;
import com.friends.model.request.*;
import com.friends.model.vo.TeamUserVO;
import com.friends.service.TeamService;
import com.friends.service.UserService;
import com.friends.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.friends.constant.UserConstant.USER_LOGIN_STATE;

@RestController
    @RequestMapping("/team")
    @CrossOrigin(origins = {"http://127.0.0.1:5173/"},allowCredentials = "true")
    @Slf4j
    public class TeamController {

        @Autowired
        private UserService userService;

        @Autowired
        private RedisTemplate redisTemplate;

        @Autowired
        private TeamService teamService;

        @Autowired
        private UserTeamService userTeamService;

        @PostMapping("/add")
        public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request){
            if (teamAddRequest == null){
                throw new BusinessException(ErrorCode.NULL_ERROR);
            }
            User logininUser = userService.getLoginUser(request);
            Team team = new Team();
            BeanUtils.copyProperties(teamAddRequest,team);
            long teamId = teamService.addTeam(team,logininUser);
            return ResultUtils.success(teamId);
        }

        /**
         * 解散队伍
         * @param id
         * @return
         */
        @PostMapping("/delete")
        public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
            if (deleteRequest == null ||deleteRequest.getId()<=0){
                throw new BusinessException(ErrorCode.NULL_ERROR);
            }
            User LoginUser=userService.getLoginUser(request);
            boolean result = teamService.deleteTeam(deleteRequest.getId(),LoginUser);

            if (!result){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
            }
            return ResultUtils.success(true);
        }

        /**
         * 更新队伍
         * @param team
         * @param request
         * @return
         */
        @PostMapping("/update")
        public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest team,HttpServletRequest request){
            if (team == null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User loginUser=userService.getLoginUser(request);
            boolean result = teamService.updateTeam(team,loginUser);
            if (!result){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
            }
            return ResultUtils.success(true);
        }

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param request
     * @return
     */
    @PostMapping("/join")
        public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request){
            if (teamJoinRequest == null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User loginUser=userService.getLoginUser(request);
            boolean result = teamService.joinTeam(teamJoinRequest,loginUser);
            return ResultUtils.success(result);
        }

        /**
     * 退出队伍
     * @param teamQuitRequest
     * @param request
     * @return
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request){
        if (teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser=userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest,loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我创建的队伍
     * @param TeamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my/create")
    public BaseResponse< List<TeamUserVO>> ListMyCreateTeam(TeamQuery teamQuery, HttpServletRequest request){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser=userService.getLoginUser(request);
        boolean isAdmin=userService.isAdmin(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVO> teamUserVOList=teamService.listTeams(teamQuery,isAdmin);
        CreateAndJoinNum(teamUserVOList);
        return ResultUtils.success(teamUserVOList);
    }


    /**
     *  获取我加入的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my/join")

    public BaseResponse<List<TeamUserVO>> listMyJoinTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User logininUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",logininUser.getId());
        List<UserTeam> userTeamlist = userTeamService.list(queryWrapper);
        if(userTeamlist.isEmpty()){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 取出不重复的队伍 id
        Map<Long, List<UserTeam>> listMap = userTeamlist.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        ArrayList<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery,true);
        if(teamList==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        CreateAndJoinNum(teamList);
        return ResultUtils.success(teamList);
    }


    /**
     * 根据ID获取队伍信息
     * @param id
     * @return
     */
    @GetMapping("/get")
        public BaseResponse<Team> getTeamById(long id){
                if (id <= 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Team team = teamService.getById(id);
            if (team == null){
                throw new BusinessException(ErrorCode.NULL_ERROR);
            }
            return ResultUtils.success(team);
        }

    /**
     * 获取队伍列表
     * @param teamQuery
     * @param request
     * @return
     */
        @GetMapping("/list")
        public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery,HttpServletRequest request) {
            if (teamQuery == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            //判断登录态
            Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
            User currentUser = (User) userObj;
            if (currentUser == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN);
            }
            boolean isAdmin=userService.isAdmin(request);
            List<TeamUserVO> teamList = teamService.listTeams(teamQuery,isAdmin);
            //2.判断当前用户是否已加入队伍,从userTeam中查找已加入的队伍
            List<Long> teamIdList=teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
            QueryWrapper queryWrapper=new QueryWrapper();
            try {
                User loginUse= userService.getLoginUser(request);
                queryWrapper.eq("userId",loginUse.getId());
                queryWrapper.in("teamId",teamIdList);
                List<UserTeam> userTeamList= userTeamService.list(queryWrapper);
                //已加入的队伍Id
                Set<Long> hasJoinTeamIdSet=userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
                teamList.forEach(team ->{
                    boolean hasJoin=hasJoinTeamIdSet.contains(team.getId());
                    team.setHasJoin(hasJoin);
                });
            }catch (Exception e) {}
            //3. 查询已加入队伍的人数
            QueryWrapper<UserTeam> userTeamQueryWrapper=new QueryWrapper<>();
            userTeamQueryWrapper.in("teamId",teamIdList);
            List<UserTeam> userTeamList=userTeamService.list(userTeamQueryWrapper);
            //队伍ID=>加入这个队伍的用户列表
            Map<Long,List<UserTeam>>  teamIdUserTeamList=userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
            teamList.forEach(team -> team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(), new ArrayList<>()).size()));
            return ResultUtils.success(teamList);
            }

        @GetMapping("/list/page")
        public BaseResponse<Page<Team>> listPageTeams(TeamQuery teamQuery) {
            if (teamQuery == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Team team = new Team();
            BeanUtils.copyProperties(teamQuery, team);
            Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
            QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
            Page<Team> resultPage = teamService.page(page,queryWrapper);
            return ResultUtils.success(resultPage);
        }

    //查询已加入队伍的人数，teamList默认是已筛选后的列表，只做查询和设置状态为已加入
    public void CreateAndJoinNum(  List<TeamUserVO> teamList){
        //3. 查询已加入队伍的人数
        List<Long> teamIdList=teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> userTeamQueryWrapper=new QueryWrapper<>();
        userTeamQueryWrapper.in("teamId",teamIdList);
        List<UserTeam> userTeamList=userTeamService.list(userTeamQueryWrapper);
        //队伍ID=>加入这个队伍的用户列表
        Map<Long,List<UserTeam>>  teamIdUserTeamList=userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        teamList.forEach(team -> team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(), new ArrayList<>()).size()));
        teamList.forEach(team ->{
            team.setHasJoin(true);
        });
    }
}

