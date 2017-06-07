package com.crumbits.Info;

/**
 *
 * @author Miquel Ferriol
 */
public class ThemeInfo extends Info{

    private String name;
    private int nreCrumbs;
    private int nreUsersFollowing;
    private boolean isFollowing;
    private FileInfo themeFile;

    /**
     *
     * @return
     */
    public FileInfo getThemeFile() {
        return themeFile;
    }

    /**
     *
     * @param themeFile
     */
    public void setThemeFile(FileInfo themeFile) {
        this.themeFile = themeFile;
    }

    /**
     *
     * @return
     */
    public int getNreCrumbs() {
        return nreCrumbs;
    }

    /**
     *
     * @param nreCrumbs
     */
    public void setNreCrumbs(int nreCrumbs) {
        this.nreCrumbs = nreCrumbs;
    }

    /**
     *
     * @return
     */
    public int getNreUsersFollowing() {
        return nreUsersFollowing;
    }

    /**
     *
     * @param nreUsersFollowing
     */
    public void setNreUsersFollowing(int nreUsersFollowing) {
        this.nreUsersFollowing = nreUsersFollowing;
    }

    /**
     *
     * @return
     */
    public boolean isIsFollowing() {
        return isFollowing;
    }

    /**
     *
     * @param isFollowing
     */
    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

}
