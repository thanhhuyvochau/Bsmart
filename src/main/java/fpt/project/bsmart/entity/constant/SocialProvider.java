package fpt.project.bsmart.entity.constant;

/**
 * @author Chinna
 * @since 26/3/18
 */
public enum SocialProvider {

    GOOGLE("google"), LOCAL("local");

    private String providerType;

    public String getProviderType() {
        return providerType;
    }

    SocialProvider(final String providerType) {
        this.providerType = providerType;
    }

}
