package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@UnifiedResponse
@RequestMapping("commit")
public class CommitController {
    @Autowired
    private CommitService commitService;

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Commit insert(@RequestBody Commit commit) {
        return commitService.insert(commit);
    }
}
