package fpt.project.bsmart.entity.constant;

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
