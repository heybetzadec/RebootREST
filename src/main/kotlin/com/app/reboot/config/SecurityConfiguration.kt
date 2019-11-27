package com.app.reboot.config

import com.app.reboot.security.JwtAuthenticationEntryPoint
import com.app.reboot.security.JwtAuthenticationProvider
import com.app.reboot.security.JwtAuthenticationTokenFilter
import com.app.reboot.security.JwtSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private val authenticationProvider: JwtAuthenticationProvider? = null
    @Autowired
    private val entryPoint: JwtAuthenticationEntryPoint? = null

    @Bean
    public override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(authenticationProvider?.let { listOf<AuthenticationProvider>(it) })
    }

    @Bean
    fun authenticationTokenFilter(): JwtAuthenticationTokenFilter {
        val filter = JwtAuthenticationTokenFilter()
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(JwtSuccessHandler())
        return filter
    }


//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//
//        http.csrf().disable()
//                .authorizeRequests().antMatchers("**/secure/**").authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(entryPoint)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
//        http.headers().cacheControl()
//    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .cors().and()
                .exceptionHandling().authenticationEntryPoint(entryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .antMatcher("**/secure/**").authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/use/**").permitAll() // this
                .anyRequest().authenticated()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }

}