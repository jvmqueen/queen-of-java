package com.selfxdsd.selfweb;

import com.selfxdsd.api.Project;
import com.selfxdsd.api.Self;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public final class ProjectsController implements Projects {

    private Self self;

    @Autowired
    public ProjectsController(final Self self) {
        this.self = self;
    }

    @GetMapping("/github/{owner}/{name}")
    public String githubProject(@PathVariable("owner") final String owner, @PathVariable("name") final String name, final Model model) {
        model.addAttribute("provider", "github");
        return "project.html";
    }

    @GetMapping("/gitlab/{owner}/{name}")
    public String gitlabProject(@PathVariable("owner") final String owner, @PathVariable("name") final String name, final Model model) {
        model.addAttribute("provider", "gitlab");
        return "project.html";
    }

    @GetMapping("/p/{owner}/{name}")
    public String badgePage(@PathVariable("owner") final String owner, @PathVariable("name") final String name, @RequestParam(name = "provider", required = false) final String provider, final Model model) {
        final String prov;
        if (provider == null || provider.isEmpty()) {
            prov = "github";
        } else {
            prov = provider;
        }
        final Project found = this.self.projects().getProjectById(owner + "/" + name, prov);
        if (found != null) {
            model.addAttribute("managed", true);
        } else {
            model.addAttribute("managed", false);
        }
        return "badge.html";
    }
}
