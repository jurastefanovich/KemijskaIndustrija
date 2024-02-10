package com.example.stefanovic.kemijskaindustrija.Controllers.Users;

import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;

public interface UserFunctionlities {

    /**
     * Method checks if the logged in user is viewing their own profile
     * @param user_id id of the user profile being viewed
     * @return boolean
     */
    default boolean checkIfEditingLoggedInUser(Long viewedUserId){
        return viewedUserId.equals(UserRepository.getLoggedInUser().getId());
    }
}
