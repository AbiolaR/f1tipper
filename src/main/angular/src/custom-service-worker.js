importScripts('./ngsw-worker.js');

(function () {
    'use strict';

    self.addEventListener('notificationclick', (event) => {
        if (clients.openWindow && event.notification.data.url) {
            event.waitUntil(clients.matchAll({
                type: "window",
                includeUncontrolled: true
            }).then(function (clientList) {
                if (event.notification.data.url) {
                    let client = null;
            
                    for (let i = 0; i < clientList.length; i++) {
                        let item = clientList[i];
            
                        if (item.url) {
                            client = item;
                            break;
                        }
                    }
            
                    if (client && 'navigate' in client) {
                        client.focus();
                        event.notification.close();
                        return client.navigate(event.notification.data.url);
                    }
                    else {
                        event.notification.close();
                        // if client doesn't have navigate function, try to open a new browser window
                        return clients.openWindow(event.notification.data.url);
                    }
                }
            }));
        }
    });}
());