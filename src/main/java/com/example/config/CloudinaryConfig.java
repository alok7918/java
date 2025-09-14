package com.example.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary(ObjectUtils.asMap(
    		 "cloud_name", "dckkzwcry",
             "api_key", "853554382881499",
             "api_secret", "ftslYAFeVHM7Ww3Bl6Na2K0MHX8"
    ));
  }
}
