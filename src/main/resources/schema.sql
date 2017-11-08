DROP TABLE IF EXISTS votes CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS threads CASCADE;
DROP TABLE IF EXISTS forums CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS forumUsers CASCADE;

DROP INDEX IF EXISTS indexUserNickname;
DROP INDEX IF EXISTS indexUserEmail;

DROP INDEX IF EXISTS indexForumSlug;

DROP INDEX IF EXISTS indexThreadForum;
DROP INDEX IF EXISTS indexThreadSlug;
DROP INDEX IF EXISTS indexThreadForumCreated;

DROP INDEX IF EXISTS indexPostCreated;
DROP INDEX IF EXISTS indexPostThread;
DROP INDEX IF EXISTS indexPostThreadPath;
DROP INDEX IF EXISTS indexPostThreadId;
DROP INDEX IF EXISTS indexPostThreadPath1;
DROP INDEX IF EXISTS indexPostPath;
DROP INDEX IF EXISTS indexPostThreadParent;
DROP INDEX IF EXISTS indexPostThreadParentId;

DROP INDEX IF EXISTS indexVoteNicknameThread;
DROP INDEX IF EXISTS indexVoteThread;

DROP INDEX IF EXISTS indexForumUsersNickname;
DROP INDEX IF EXISTS indexForumUsersForum;


CREATE TABLE IF NOT EXISTS users (
  about    TEXT        NOT NULL,
  email    TEXT UNIQUE NOT NULL,
  fullname TEXT        NOT NULL,
  nickname TEXT UNIQUE NOT NULL PRIMARY KEY
);

CREATE UNIQUE INDEX IF NOT EXISTS indexUserNickname ON users(LOWER(nickname));
CREATE UNIQUE INDEX IF NOT EXISTS indexUserEmail ON users(LOWER(email));


CREATE TABLE IF NOT EXISTS forums (
  posts   BIGINT,
  slug    TEXT NOT NULL UNIQUE,
  threads INTEGER,
  title   TEXT NOT NULL,
  "user"  TEXT NOT NULL UNIQUE REFERENCES users (nickname)
);

CREATE UNIQUE INDEX IF NOT EXISTS indexForumSlug ON forums(LOWER(slug));


CREATE TABLE IF NOT EXISTS threads (
  author  TEXT NOT NULL REFERENCES users (nickname),
  created TIMESTAMP DEFAULT current_timestamp,
  forum   TEXT NOT NULL REFERENCES forums (slug),
  id      SERIAL PRIMARY KEY,
  message TEXT NOT NULL,
  slug    TEXT UNIQUE,
  title   TEXT NOT NULL,
  votes   INTEGER
);

CREATE INDEX IF NOT EXISTS indexThreadForum ON threads(LOWER(forum));
CREATE UNIQUE INDEX IF NOT EXISTS indexThreadSlug ON threads(LOWER(slug));
CREATE INDEX IF NOT EXISTS indexThreadForumCreated ON threads(LOWER(forum), created);


CREATE TABLE IF NOT EXISTS posts (
  author   TEXT    NOT NULL REFERENCES users (nickname),
  created  TIMESTAMP        DEFAULT current_timestamp,
  forum    TEXT    NOT NULL REFERENCES forums (slug),
  id       SERIAL PRIMARY KEY,
  isEdited BOOLEAN NOT NULL DEFAULT FALSE,
  message  TEXT    NOT NULL,
  parent   BIGINT  NOT NULL,
  thread   INTEGER NOT NULL REFERENCES threads (id),
  path     INT ARRAY
);

CREATE INDEX IF NOT EXISTS indexPostCreated ON posts(created);
CREATE INDEX IF NOT EXISTS indexPostThread ON posts(thread);
CREATE INDEX IF NOT EXISTS indexPostThreadPath ON posts(thread, path);
CREATE INDEX IF NOT EXISTS indexPostThreadId ON posts(thread, id);
CREATE INDEX IF NOT EXISTS indexPostThreadPath1 ON posts(thread, (path[1]));
CREATE INDEX IF NOT EXISTS indexPostPath ON posts((path[1]));
CREATE INDEX IF NOT EXISTS indexPostThreadParent ON posts(thread, parent);
CREATE INDEX IF NOT EXISTS indexPostThreadParentId ON posts(thread, parent, id);


CREATE TABLE IF NOT EXISTS votes (
  id       SERIAL PRIMARY KEY,
  nickname TEXT     NOT NULL REFERENCES users (nickname),
  voice    SMALLINT NOT NULL,
  thread   INTEGER  NOT NULL REFERENCES threads (id),
  UNIQUE (nickname, thread)
);

CREATE UNIQUE INDEX indexVoteNicknameThread ON votes(nickname, thread);
CREATE INDEX indexVoteThread ON votes(thread);


CREATE TABLE IF NOT EXISTS forumUsers (
  userNickname TEXT NOT NULL REFERENCES users (nickname),
  forumSlug    TEXT NOT NULL REFERENCES forums (slug),
  UNIQUE (userNickname, forumSlug)
);

CREATE INDEX IF NOT EXISTS indexForumUsersNickname ON forumUsers(LOWER(userNickname));
CREATE INDEX IF NOT EXISTS indexForumUsersForum ON forumUsers(LOWER(forumSlug));