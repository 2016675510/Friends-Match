package com.friends.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friends.model.domain.Team;
import com.friends.model.domain.User;
import com.friends.model.request.TeamJoinRequest;
import com.friends.model.request.TeamQuery;
import com.friends.model.request.TeamQuitRequest;
import com.friends.model.request.TeamUpdateRequest;
import com.friends.model.vo.TeamUserVO;

import java.util.List;

/**
 *
 */
public interface TeamService extends IService<Team> {
    /**
     *   添加队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
    /**
     * 搜索队伍
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     * @param team
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest team, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);
    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 解散队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);


}
