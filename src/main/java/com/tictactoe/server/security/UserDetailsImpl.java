package com.tictactoe.server.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tictactoe.server.models.Player;

import io.jsonwebtoken.lang.Collections;
import lombok.Getter;

public class UserDetailsImpl implements UserDetails{
    @Getter
    private Player player;

    public UserDetailsImpl(Player player){
        this.player = player;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return player.getPassword();
    }

    @Override
    public String getUsername() {
        return player.getNickname();
    }


}
