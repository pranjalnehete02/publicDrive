package cdac.acts.drive.dto;

public class FileFolderDTO {
    private String fname;
    private Long fparent;  // Changed from String to Long (parent folder ID)
    private boolean isfolder;
    private String owner;
    private Long fid;
    public FileFolderDTO(String fname, Long fparent, boolean isfolder, String owner, Long fid) {
        this.fname = fname;
        this.fparent = fparent;
        this.isfolder = isfolder;
        this.owner = owner;
        this.fid = fid;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    // Getters and setters
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Long getFparent() {
        return fparent;
    }

    public void setFparent(Long fparent) {
        this.fparent = fparent;
    }

    public boolean isIsfolder() {
        return isfolder;
    }

    public void setIsfolder(boolean isfolder) {
        this.isfolder = isfolder;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "FileFolderDTO{" +
                "fname='" + fname + '\'' +
                ", fparent=" + fparent +
                ", isfolder=" + isfolder +
                '}';
    }
}

