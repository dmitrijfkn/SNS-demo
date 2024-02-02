package com.kostenko.demo.proxy.seller.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Configuration class for MongoDB-related settings, enabling auditing support.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

}