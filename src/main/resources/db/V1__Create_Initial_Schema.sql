-- td_users
CREATE TABLE td_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- td_friends
CREATE TABLE td_friends (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    UNIQUE (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES td_users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES td_users(id) ON DELETE CASCADE
);

-- td_channels
CREATE TABLE td_channels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    owner_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (owner_id) REFERENCES td_users(id) ON DELETE CASCADE
);

-- tc_channel_members
CREATE TABLE tc_channel_members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    channel_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(20) NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (channel_id) REFERENCES td_channels(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES td_users(id) ON DELETE CASCADE
);

-- td_messages
CREATE TABLE td_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    recipient_id INT NULL,
    channel_id INT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES td_users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES td_users(id) ON DELETE SET NULL,
    FOREIGN KEY (channel_id) REFERENCES td_channels(id) ON DELETE CASCADE
);
