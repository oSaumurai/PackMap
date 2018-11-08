const functions = require('firebase-functions');
const firebase = require('firebase-admin');

const API_KEY = 'BE7Q7F9cdk-tfzs2Z0FoUQfehxj_519hxyIWku0t5vZbtYPc2aBGeK3M984rKv9R1yz8VbKWyuo7OqF59EpngY8';
firebase.initializeApp();

const base_message = 'You have been added to ';

exports.createGroupNotification = functions.firestore
    .document('groups/{groupId}')
    .onCreate((snap, context) => {
        // Get an object representing the document
        // e.g. {'name': 'Marie', 'age': 66}
        const newGroup = snap.data();

        // access a particular field as you would any JS property
        const name = newGroup.name;
        const userList = newGroup.userList;

        // perform desired operations ...
        for (let i = 0; i < userList.length; i++) {
            sendNotificationToUser(userList[i], name)
        }
    });

function sendNotificationToUser(user, groupName) {
    const message = {
        notification: {
            body: base_message + groupName
        },
        android: {
            ttl: 3600 * 1000, // 1 hour in milliseconds
            priority: 'normal'
        },
        topic: '/topics/user_' + user
    };
    firebase.messaging().send(message)
        .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
            return response;
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
}
