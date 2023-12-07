package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class SpendRequestController {

    private final UserDao userDao;
    private final JdbcSpendRequestDao jdbcSpendRequestDao;
    private final JdbcVoteDao jdbcVoteDao;
    private final JdbcUserDao jdbcUserDao;

    private final JdbcCampaignDao jdbcCampaignDao;

    public SpendRequestController(UserDao userDao,
                                  JdbcSpendRequestDao jdbcSpendRequestDao,
                                  JdbcVoteDao jdbcVoteDao,
                                  JdbcUserDao jdbcUserDao,
                                  JdbcCampaignDao jdbcCampaignDao) {
        this.userDao = userDao;
        this.jdbcSpendRequestDao = jdbcSpendRequestDao;
        this.jdbcVoteDao = jdbcVoteDao;
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcCampaignDao = jdbcCampaignDao;
    }

    @GetMapping("/spend-requests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SpendRequest getSpendRequestById(@PathVariable int id, Principal principal) {

        // TODO need to restrict non-donors from access to spend request.
        SpendRequest spendRequest = jdbcSpendRequestDao.getSpendRequestById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spend request not found.");
        });

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this spend request.");
        }

        int requestCampaignId = spendRequest.getCampaignId();
        int donorId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);

        List<Campaign> donorCampaigns = jdbcCampaignDao.getCampaignsByDonorId(donorId);

        for (Campaign campaign : donorCampaigns) {
            if (campaign.getId() != requestCampaignId) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                            "not authorized to view this campaign.");
                }
            } return spendRequest;
        }

    @GetMapping("/campaigns/{campaignId}/spend-requests")
    @ResponseStatus(HttpStatus.OK)
    public List<SpendRequest> getSpendReqByCampaignId (Principal principal, @PathVariable int campaignId) {

        //TODO Works, but also needs security so that only donors can view.
        // Optional<User> loggedInUser = userDao.getUserById(userId);
       /* Optional<User> loggedInUser = userDao.getUserById(userId);
        if (loggedInUser.isPresent() && !loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view these spend requests.");
        }*/
        return new ArrayList<>(jdbcSpendRequestDao.getSpendRequestsByCampaign(campaignId));
    }


    // TODO doesn't work yet.
    @GetMapping("/campaigns/{campaignId}/spend-request/{spend-req-id}/votes")
    @ResponseStatus(HttpStatus.OK)
    public List<Vote> getVotesBySpendReq(Principal principal, @PathVariable int userId, @PathVariable int campId, @PathVariable int spendReqId) {
        Optional<User> loggedInUser = userDao.getUserById(userId);

        if (loggedInUser.isPresent() && !loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get these votes.");
        }
        return new ArrayList<>(jdbcVoteDao.getVotesBySpendRequestId(spendReqId));
    }

    @PostMapping("/campaigns/{campaignId}/spend-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendRequest newRequest (@Valid @RequestBody NewSpendRequestDto newSpendRequestDto, @PathVariable int campaignId) {
        return jdbcSpendRequestDao.createSpendRequest(newSpendRequestDto);
    }

    public boolean isCorrectUser(Principal principal, NewDonationDto newDonationDto) {
        String username = principal.getName();
        Optional<User> optionalUser = userDao.getUserByUsername(username);
        User user = optionalUser.orElseThrow();
        int loggedInUserID = user.getId();
        return loggedInUserID == newDonationDto.getDonorId();
    }
}
