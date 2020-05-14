package com.company.website.controller.constants;

/**
 * @author Dmitry Matrizaev
 * @since 14.05.2020
 */
public final class ControllerConstants {

    private ControllerConstants() {
    }

    public static final String REDIRECT_TO_ADMIN = "redirect:/admin";
    public static final String ADMIN_HOME = "admin/adminHome";
    public static final String REDIRECT_TO_CATEGORY = "redirect:/admin/%s";
    public static final String CATEGORY = "admin/adminCategory";
    public static final String REDIRECT_PROJECT = "redirect:/admin/%s/%s/%s";
    public static final String ADMIN_PROJECT = "admin/adminProject";
    public static final String REDIRECT_SUBGROUP = "redirect:/admin/%s/%s";
    public static final String ADMIN_SUBGROUP = "admin/adminSubgroup";
    public static final String REGISTRATION = "registration";
    public static final String REDIRECT_TO_MAIN = "redirect:/";
    public static final String USER_HOMEPAGE = "user/userHome";
    public static final String USER_CATEGORY = "user/userCategory";
    public static final String USER_SUBGROUP = "user/userSubgroup";
    public static final String USER_PROJECT = "user/userProject";

}
