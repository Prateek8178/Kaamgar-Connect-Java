package com.kaamgar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class EmailService {

    @Value("${resend.api.key:}")
    private String resendApiKey;

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // ── OTP Email ──────────────────────────────────────────────────────────────
    public void sendOtpEmail(String toEmail, String username, String otp) {
        String html = "<div style='font-family:sans-serif;max-width:500px'>"
            + "<h2 style='color:#6C63FF'>Kaamgar Connect</h2>"
            + "<p>Hi <strong>" + username + "</strong>,</p>"
            + "<p>Your OTP verification code is:</p>"
            + "<div style='font-size:2.5rem;font-weight:900;letter-spacing:12px;"
            + "color:#6C63FF;padding:20px 0;text-align:center'>" + otp + "</div>"
            + "<p style='color:#888'>Expires in <strong>10 minutes</strong>. Do not share it.</p>"
            + "</div>";
        sendViaResend(toEmail, "[Kaamgar Connect] OTP: " + otp, html);
    }

    // ── Password Reset Email ───────────────────────────────────────────────────
    public void sendPasswordResetEmail(String toEmail, String username, String otp) {
        String html = "<div style='font-family:sans-serif;max-width:500px'>"
            + "<h2 style='color:#E74C3C'>Password Reset</h2>"
            + "<p>Hi <strong>" + username + "</strong>,</p>"
            + "<p>Your password reset OTP is:</p>"
            + "<div style='font-size:2.5rem;font-weight:900;letter-spacing:12px;"
            + "color:#E74C3C;padding:20px 0;text-align:center'>" + otp + "</div>"
            + "<p style='color:#888'>Expires in <strong>10 minutes</strong>.</p>"
            + "</div>";
        sendViaResend(toEmail, "[Kaamgar Connect] Password Reset OTP", html);
    }

    // ── Application Notification ───────────────────────────────────────────────
    public void sendApplicationNotification(String toEmail, String employerName,
                                            String jobTitle, String workerName) {
        String html = "<div style='font-family:sans-serif;max-width:500px'>"
            + "<h2 style='color:#6C63FF'>New Application!</h2>"
            + "<p>Hi <strong>" + employerName + "</strong>,</p>"
            + "<p><strong>" + workerName + "</strong> applied for: <strong>" + jobTitle + "</strong></p>"
            + "<p>Login to review the application.</p></div>";
        sendViaResend(toEmail, "[Kaamgar Connect] New Application for " + jobTitle, html);
    }

    // ── Resend HTTP API ────────────────────────────────────────────────────────
    private void sendViaResend(String to, String subject, String html) {
        if (resendApiKey == null || resendApiKey.isBlank()) {
            System.err.println("⚠️  RESEND_API_KEY not set — email skipped for: " + to);
            return;
        }
        try {
            // Escape double quotes in html for JSON
            String safeHtml = html.replace("\\", "\\\\").replace("\"", "\\\"");
            String json = "{"
                + "\"from\":\"Kaamgar Connect <onboarding@resend.dev>\","
                + "\"to\":[\"" + to + "\"],"
                + "\"subject\":\"" + subject + "\","
                + "\"html\":\"" + safeHtml + "\""
                + "}";

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() == 200 || resp.statusCode() == 201) {
                System.out.println("✅ Email sent via Resend to " + to);
            } else {
                System.err.println("❌ Resend error " + resp.statusCode() + ": " + resp.body());
            }
        } catch (Exception e) {
            System.err.println("❌ Email send failed: " + e.getMessage());
        }
    }
}
