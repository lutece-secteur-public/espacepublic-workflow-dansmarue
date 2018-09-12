/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.dansmarue.task.notificationsuivi.business;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

public class NotificationSuiviTaskConfig extends TaskConfig {
    private String subject;
    private String sender;
    private String mobileSubject;
    private String mailMessage;
    private String mobileMessage;

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }
    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
    /**
     * @return the mailMessage
     */
    public String getMailMessage() {
        return mailMessage;
    }
    /**
     * @param mailMessage the mailMessage to set
     */
    public void setMailMessage(String mailMessage) {
        this.mailMessage = mailMessage;
    }
    /**
     * @return the mobileMessage
     */
    public String getMobileMessage() {
        return mobileMessage;
    }
    /**
     * @param mobileMessage the mobileMessage to set
     */
    public void setMobileMessage(String mobileMessage) {
        this.mobileMessage = mobileMessage;
    }
    /**
     * @return the mobileSubject
     */
    public String getMobileSubject() {
        return mobileSubject;
    }
    /**
     * @param mobileSubject the mobileSubject to set
     */
    public void setMobileSubject(String mobileSubject) {
        this.mobileSubject = mobileSubject;
    }
}
