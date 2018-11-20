const functions = require('firebase-functions');
const firebase = require('firebase-admin');

firebase.initializeApp();

const base_new_group_message = 'You have been added to ';
const base_new_activity_message = 'A new activity has been created for ';

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
            sendNotificationToUser('/topics/user_' + userList[i], name, base_new_group_message);
        }
    });

exports.createActivityNotification = functions.firestore
    .document('groups/{groupId}')
    .onUpdate((change, context) => {
        // Get an object representing the document
        // e.g. {'name': 'Marie', 'age': 66}
        const newValue = change.after.data();

        // ...or the previous value before this update
        const prevValue = change.before.data();

        // access a particular field as you would any JS property
        const newGroups = newValue.activityList;
        const prevGroups = prevValue.activityList;
        const groupName = newValue.name;
        const groupId = context.params.groupId;

        // perform desired operations ...
        const difference = newGroups.filter(x => !prevGroups.includes(x));
        for (let i = 0; i < difference.length; i++) {
            sendNotificationToUser('/topics/group_' + groupId, groupName, base_new_activity_message);
        }
    });

function sendNotificationToUser(topic, rest_of_message, base_message) {
    const message = {
        notification: {
            body: base_message + rest_of_message
        },
        android: {
            ttl: 3600 * 1000, // 1 hour in milliseconds
            priority: 'normal'
        },
        topic: topic
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
