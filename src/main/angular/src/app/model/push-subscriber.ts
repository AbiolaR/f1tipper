export class PushSubscriber{
    endpoint: string;
    p256dh: string;
    auth: string;
    username: string;

    constructor (endpoint: string, p256dh: string, auth: string, username: string) {
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.auth = auth;
        this.username = username;
    } 
}