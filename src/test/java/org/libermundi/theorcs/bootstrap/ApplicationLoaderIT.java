package org.libermundi.theorcs.bootstrap;

import com.google.common.collect.Iterators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.listeners.CreatedOrUpdatedByListener;
import org.libermundi.theorcs.services.AuthorityService;
import org.libermundi.theorcs.services.UserService;
import org.libermundi.theorcs.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationLoaderIT {

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Test
    @Transactional
    public void onApplicationEvent() throws Exception {
        assertEquals(4,Iterators.size(userService.findAll().iterator()));
        assertEquals(4,Iterators.size(authorityService.findAll().iterator()));

        User root = userService.findByUsername("root");

        assertNotNull(root.getCreatedDate());
        assertNotNull(root.getUpdatedDate());
        assertEquals(SecurityConstants.USERNAME_SYSTEM, root.getCreatedBy());
        assertEquals(SecurityConstants.USERNAME_SYSTEM, root.getUpdatedBy());
    }

}