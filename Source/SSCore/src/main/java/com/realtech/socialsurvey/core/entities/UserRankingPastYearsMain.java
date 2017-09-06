package com.realtech.socialsurvey.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name= "user_ranking_past_years_main")
public class UserRankingPastYearsMain
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    @Column ( name = "user_ranking_past_years_main_id")
    private String userRankingPastYearsMainId;
    
    @Column ( name = "user_id")
    private long userId;
    
    @Column( name = "company_id")
    private long companyId;
    
    @Column( name = "region_id")
    private long regionId;
    
    @Column( name = "branch_id")
    private long branchId;
    @Column( name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email_id")
    private String emailId;
    
    @Column(name = "ranking_score")
    private float rankingScore;
    
    @Column(name = "rank")
    private int rank;
    
    @Column(name = "sps")
    private float sps;
    
    @Column(name = "days_of_registration")
    private int daysOfRegistration;
    
    @Column(name = "completed")
    private int completed;
    
    @Column(name = "sent")
    private int sent;
    
    @Column(name = "completed_percentage")
    private float completedPercentage;
    
    @Column(name = "total_reviews")
    private int totalReviews;
    
    @Column(name = "average_rating")
    private int averageRating;
    
    @Column(name = "is_eligible")
    private	int isEligible;

    @Column(name = "nmls_id")
    private String nmlsId; 
    
    @Transient
    private String profileImageUrlThumbnail;
    
    public String getUserRankingPastYearsMainId()
    {
        return userRankingPastYearsMainId;
    }

    public void setUserRankingPastYearsMainId( String userRankingPastYearsMainId )
    {
        this.userRankingPastYearsMainId = userRankingPastYearsMainId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId( long userId )
    {
        this.userId = userId;
    }

    public long getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId( long companyId )
    {
        this.companyId = companyId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    
    public String getEmailId()
    {
        return emailId;
    }

    public void setEmailId( String emailId )
    {
        this.emailId = emailId;
    }

    public float getRankingScore()
    {
        return rankingScore;
    }

    public void setRankingScore( float rankingScore )
    {
        this.rankingScore = rankingScore;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank( int rank )
    {
        this.rank = rank;
    }

    public float getSps()
    {
        return sps;
    }

    public void setSps( float sps )
    {
        this.sps = sps;
    }

    public int getDaysOfRegistration()
    {
        return daysOfRegistration;
    }

    public void setDaysOfRegistration( int daysOfRegistration )
    {
        this.daysOfRegistration = daysOfRegistration;
    }

    public int getCompleted()
    {
        return completed;
    }

    public void setCompleted( int completed )
    {
        this.completed = completed;
    }

    public int getSent()
    {
        return sent;
    }

    public void setSent( int sent )
    {
        this.sent = sent;
    }

    public float getCompletedPercentage()
    {
        return completedPercentage;
    }

    public void setCompletedPercentage( float completedPercentage )
    {
        this.completedPercentage = completedPercentage;
    }

    public int getTotalReviews()
    {
        return totalReviews;
    }

    public void setTotalReviews( int totalReviews )
    {
        this.totalReviews = totalReviews;
    }

    public int getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating( int averageRating )
    {
        this.averageRating = averageRating;
    }

    public int getIsEligible() {
		return isEligible;
	}

	public void setIsEligible(int isEligible) {
		this.isEligible = isEligible;
	}

	
	public long getRegionId()
    {
        return regionId;
    }

    public void setRegionId( long regionId )
    {
        this.regionId = regionId;
    }

    public long getBranchId()
    {
        return branchId;
    }

    public void setBranchId( long branchId )
    {
        this.branchId = branchId;
    }

    public String getNmlsId() {
  		return nmlsId;
  	}

  	public void setNmlsId(String nmlsId) {
  		this.nmlsId = nmlsId;
  	}

    public String getProfileImageUrlThumbnail()
    {
        return profileImageUrlThumbnail;
    }

    public void setProfileImageUrlThumbnail( String profileImageUrlThumbnail )
    {
        this.profileImageUrlThumbnail = profileImageUrlThumbnail;
    }

    @Override
    public String toString()
    {
        return "UserRankingPastYearsMain [userRankingPastYearsMainId=" + userRankingPastYearsMainId + ", userId=" + userId
            + ", companyId=" + companyId + ", regionId=" + regionId + ", branchId=" + branchId + ", firstName=" + firstName
            + ", lastName=" + lastName + ", emailId=" + emailId + ", rankingScore=" + rankingScore + ", rank=" + rank + ", sps="
            + sps + ", daysOfRegistration=" + daysOfRegistration + ", completed=" + completed + ", sent=" + sent
            + ", completedPercentage=" + completedPercentage + ", totalReviews=" + totalReviews + ", averageRating="
            + averageRating + ", isEligible=" + isEligible + ", nmlsId=" + nmlsId + ", profileImageUrlThumbnail="
            + profileImageUrlThumbnail + "]";
    }
    
    

}
