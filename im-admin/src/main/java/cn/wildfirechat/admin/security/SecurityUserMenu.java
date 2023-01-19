package cn.wildfirechat.admin.security;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class SecurityUserMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private String api;
    private Integer order;
    private List<SecurityUserMenu> children;

    public SecurityUserMenu() {
        super();
        this.children = new LinkedList<>();
    }

    public SecurityUserMenu(Long id, Long parentId, String name, String code, String api, Integer order) {
        this();
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.api = api;
        this.order = order;
    }

    public void addChild(SecurityUserMenu menu) {
        this.children.add(menu);
    }

}
