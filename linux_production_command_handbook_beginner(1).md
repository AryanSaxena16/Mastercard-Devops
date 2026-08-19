# Linux Production Command Handbook --- Beginner Friendly

## How to use this guide

This guide is written for someone who is **new to Linux** but needs to
become comfortable working on production servers.

For every command, try to understand four things:

1.  **What is it?**
2.  **What does the command actually do?**
3.  **When would I use it in production?**
4.  **What should I run before/after it?**

> **Important:** Never run a production command just because you know
> the syntax. First understand what it will change.

------------------------------------------------------------------------

# 1. The Mental Model: What Am I Actually Working With?

A Linux server can be thought of as several layers:

``` text
                    LINUX SERVER
                         |
       +-----------------+-----------------+
       |                 |                 |
       v                 v                 v
    FILES             PROCESSES          NETWORK
       |                 |                 |
       |                 |                 |
   permissions        services         IP / ports
   ownership          CPU / RAM        DNS
   logs               systemd          routing
   disk               PIDs            firewall
       |                 |                 |
       +-----------------+-----------------+
                         |
                         v
                    APPLICATION
```

When an application fails, ask:

``` text
Is the file/configuration correct?
        |
        v
Is the process running?
        |
        v
Is the service healthy?
        |
        v
Are logs showing an error?
        |
        v
Is there enough CPU/RAM/disk?
        |
        v
Is the network working?
        |
        v
Is DNS resolving?
        |
        v
Is the required port reachable?
```

This is the basic production troubleshooting mindset.

------------------------------------------------------------------------

# 2. Before Running Anything in Production

First identify yourself and the server.

## `whoami`

### What is it?

Shows the Linux user you are currently logged in as.

``` bash
whoami
```

Example:

``` text
aditya
```

### Why do I care?

Linux permissions depend heavily on **which user you are**.

If:

``` text
aditya
```

runs a command, the command may behave differently from when:

``` text
root
```

runs it.

### Production scenario

You receive:

> "You don't have permission to modify this configuration."

First check:

``` bash
whoami
```

Then:

``` bash
id
```

------------------------------------------------------------------------

# 3. `hostname`

Shows the server's hostname.

``` bash
hostname
```

### Why?

You may SSH into multiple servers:

``` text
dev-server
test-server
prod-server
database-server
```

Before making changes, make sure you are on the correct machine.

``` bash
hostname
whoami
```

Think:

``` text
WHO am I?
     +
WHERE am I?
     |
     v
Now I can safely investigate.
```

------------------------------------------------------------------------

# 4. Filesystem Navigation

Linux stores files in a tree.

``` text
/
├── etc       configuration
├── home      user homes
├── var
│   └── log   logs
├── opt       optional/custom applications
├── usr       applications and libraries
├── tmp       temporary files
├── root      root user's home
├── dev       devices
├── proc      process/kernel information
└── run       runtime information
```

------------------------------------------------------------------------

## `pwd`

### Meaning

**Print Working Directory**

``` bash
pwd
```

Example:

``` text
/opt/myapp
```

It tells you:

> "This is the directory I am currently inside."

### Production use

Before deleting, moving or editing anything:

``` bash
pwd
ls -la
```

This prevents mistakes caused by being in the wrong directory.

------------------------------------------------------------------------

## `ls`

Lists files and directories.

``` bash
ls
```

Useful versions:

``` bash
ls -l
ls -la
ls -lh
ls -lah
```

### What they mean

``` text
-l  detailed information
-a  include hidden files
-h  human-readable sizes
```

So:

``` bash
ls -lah
```

means:

> Show all files, including hidden files, with detailed information and
> readable sizes.

### Production use

Inspect an application directory:

``` bash
cd /opt/myapp
ls -lah
```

------------------------------------------------------------------------

## `cd`

Changes directory.

``` bash
cd /var/log
```

Now:

``` bash
pwd
```

might show:

``` text
/var/log
```

### Useful forms

``` bash
cd ..
```

Go one directory up.

``` bash
cd ~
```

Go to your home directory.

``` bash
cd /
```

Go to the root of the filesystem.

------------------------------------------------------------------------

## `tree`

Shows directories in a tree structure.

``` bash
tree
```

### Production use

If you are given an unfamiliar application:

``` bash
cd /opt/myapp
tree
```

You might see:

``` text
myapp
├── bin
├── config
├── logs
├── scripts
└── data
```

This gives you a quick understanding of the application's structure.

------------------------------------------------------------------------

## `file`

Tells you what kind of file something is.

``` bash
file application
```

It might tell you:

``` text
ELF 64-bit executable
```

or:

``` text
ASCII text
```

### Production use

You receive a file called:

``` text
backup
```

but don't know whether it is an executable, text file, archive, etc.

Run:

``` bash
file backup
```

------------------------------------------------------------------------

## `stat`

Shows detailed file metadata.

``` bash
stat file.txt
```

You can see:

-   owner
-   group
-   permissions
-   size
-   access time
-   modification time
-   inode information

### Production use

You suspect a configuration file was modified.

``` bash
stat application.conf
```

Check its timestamps and ownership.

------------------------------------------------------------------------

# 5. Finding Files

## `find`

Searches the filesystem.

Basic:

``` bash
find /var/log -name "*.log"
```

Find files larger than 1 GB:

``` bash
find /var -type f -size +1G
```

Find recently modified files:

``` bash
find /var/log -type f -mtime -1
```

### Production scenario

Your server is running out of disk space.

You need to find unusually large files:

``` bash
sudo find /var -type f -size +1G -exec ls -lh {} \;
```

Flow:

``` text
Disk full
   |
   v
df -h
   |
   v
Which filesystem?
   |
   v
du
   |
   v
Which directory?
   |
   v
find
   |
   v
Which file?
```

------------------------------------------------------------------------

## `locate`

Searches for filenames quickly.

``` bash
locate application.conf
```

### Difference from `find`

`locate` normally searches a prebuilt database, so it can be much
faster, but the database may not contain a very recently created file.

Use:

``` bash
find
```

when you need a current filesystem search.

------------------------------------------------------------------------

## `which`

Shows which executable will normally be used.

``` bash
which node
```

Example:

``` text
/usr/bin/node
```

### Production scenario

Application works on one server but not another.

Check:

``` bash
which node
node --version
```

Maybe the servers are using different Node.js installations.

------------------------------------------------------------------------

## `whereis`

Shows locations related to a command.

``` bash
whereis node
```

Useful when investigating installed software and its binary/man/source
locations.

------------------------------------------------------------------------

# 6. Creating and Managing Files

## `touch`

Creates an empty file if it does not exist.

``` bash
touch test.txt
```

It can also update the timestamp of an existing file.

### Production use

Useful for testing permissions:

``` bash
touch test.txt
ls -l test.txt
```

------------------------------------------------------------------------

## `mkdir`

Creates a directory.

``` bash
mkdir logs
```

Nested directories:

``` bash
mkdir -p /opt/myapp/logs/archive
```

`-p` creates missing parent directories as required.

### Production use

Create an application log/archive directory.

------------------------------------------------------------------------

## `cp`

Copies files.

``` bash
cp application.conf application.conf.backup
```

Copy a directory:

``` bash
cp -r myapp myapp-backup
```

### Production scenario

Before editing a production configuration:

``` bash
cp application.conf application.conf.backup
```

Then edit.

------------------------------------------------------------------------

## `mv`

Moves or renames files.

``` bash
mv old.conf new.conf
```

### Production use

A common deployment technique is to replace or rename configuration
files carefully.

Always inspect first:

``` bash
pwd
ls -l
```

------------------------------------------------------------------------

## `rm`

Deletes a file.

``` bash
rm test.txt
```

Delete a directory recursively:

``` bash
rm -r old-directory
```

Force recursive deletion:

``` bash
rm -rf old-directory
```

### ⚠️ Extremely important

`rm -rf` does not ask for confirmation in many situations.

Before using it:

``` bash
pwd
ls -la
```

Make absolutely sure you are deleting the intended target.

Never casually run:

``` bash
rm -rf *
```

on a production server.

------------------------------------------------------------------------

# 7. Reading Files

## `cat`

Prints an entire file.

``` bash
cat /etc/hosts
```

Good for small files.

Don't use it as your first choice for a 20 GB log.

------------------------------------------------------------------------

## `less`

Lets you inspect large files page by page.

``` bash
less application.log
```

Useful keys:

``` text
Space       next page
b           previous page
/ERROR      search
q           quit
```

### Production use

Inspect a large log without dumping the entire thing into the terminal.

------------------------------------------------------------------------

## `more`

Another paginated file viewer.

``` bash
more application.log
```

`less` is generally more capable.

------------------------------------------------------------------------

## `head`

Shows the beginning of a file.

``` bash
head application.log
```

Show first 100 lines:

``` bash
head -100 application.log
```

### Production use

Inspect the beginning of a configuration or log.

------------------------------------------------------------------------

## `tail`

Shows the end of a file.

``` bash
tail application.log
```

Last 100 lines:

``` bash
tail -100 application.log
```

### Why is this useful?

The newest errors are often near the end of a log.

------------------------------------------------------------------------

## `tail -f`

Follows a file continuously.

``` bash
tail -f application.log
```

You see new lines as the application writes them.

### Production scenario

You restart an application and want to see what happens:

``` text
Terminal 1:
systemctl restart myapp

Terminal 2:
tail -f /path/to/application.log
```

You can watch startup errors live.

------------------------------------------------------------------------

# 8. Searching Text

## `grep`

Searches for text.

``` bash
grep "ERROR" application.log
```

Case insensitive:

``` bash
grep -i "error" application.log
```

Line numbers:

``` bash
grep -n "ERROR" application.log
```

Recursive:

``` bash
grep -r "connection refused" /var/log/
```

Exclude matching lines:

``` bash
grep -v "INFO" application.log
```

Multiple patterns:

``` bash
grep -E "ERROR|WARN|FATAL" application.log
```

### Production flow

``` text
Application failing
       |
       v
Find logs
       |
       v
grep "ERROR"
       |
       v
Find exact error
       |
       v
Investigate cause
```

------------------------------------------------------------------------

# 9. Pipes --- `|`

A pipe takes the output of one command and sends it to another command.

Example:

``` bash
ps aux | grep nginx
```

Think:

``` text
ps aux
  |
  | lots of processes
  v
grep nginx
  |
  v
only nginx-related lines
```

Another example:

``` bash
cat application.log | grep ERROR
```

Pipes are one of the most important Linux concepts because production
troubleshooting often combines small commands.

------------------------------------------------------------------------

# 10. Redirection

## `>`

Write command output to a file, replacing existing content.

``` bash
command > output.txt
```

## `>>`

Append instead of replacing.

``` bash
command >> output.txt
```

## `2>`

Redirect error output.

``` bash
command 2> error.txt
```

## `2>&1`

Send errors to the same place as normal output.

``` bash
command > output.txt 2>&1
```

### Production example

A backup script:

``` bash
./backup.sh >> /var/log/backup.log 2>&1
```

This keeps a record of both successful output and errors.

------------------------------------------------------------------------

# 11. File Permissions --- The Foundation

Linux permissions answer:

> "Who is allowed to do what with this file?"

There are three permission groups:

``` text
Owner
Group
Others
```

And three basic permissions:

``` text
r = read
w = write
x = execute
```

Example:

``` text
-rwxr-xr--
```

Break it down:

``` text
-    rwx    r-x    r--
|     |      |      |
type  owner  group  others
```

So:

``` text
Owner  = rwx
Group  = r-x
Others = r--
```

------------------------------------------------------------------------

# 12. Numeric Permissions

Linux maps permissions to numbers:

``` text
read    = 4
write   = 2
execute = 1
```

Add them:

``` text
rwx = 4 + 2 + 1 = 7
rw- = 4 + 2     = 6
r-x = 4 + 1     = 5
r-- = 4         = 4
```

Therefore:

``` bash
chmod 755 script.sh
```

means:

``` text
Owner  = 7 = rwx
Group  = 5 = r-x
Others = 5 = r-x
```

So the resulting permission is:

``` text
rwxr-xr-x
```

------------------------------------------------------------------------

# 13. `chmod`

Changes permissions.

``` bash
chmod 755 script.sh
```

### Symbolic form

``` bash
chmod u+x script.sh
chmod g+x script.sh
chmod o-w file.txt
```

Where:

``` text
u = owner/user
g = group
o = others
```

### Production scenario

You deploy a script:

``` bash
./deploy.sh
```

and receive:

``` text
Permission denied
```

Inspect:

``` bash
ls -l deploy.sh
```

If it is not executable:

``` bash
chmod u+x deploy.sh
```

Then:

``` bash
./deploy.sh
```

Do not immediately use:

``` bash
chmod 777 deploy.sh
```

------------------------------------------------------------------------

# 14. Why `chmod 777` Is Dangerous

``` bash
chmod 777 file
```

means:

``` text
Owner  -> rwx
Group  -> rwx
Others -> rwx
```

Everyone gets full permissions.

A production security model should normally look more like:

``` text
Application user -> required access
Application group -> required shared access
Everyone else -> minimum/no access
```

If you see:

``` text
Permission denied
```

investigate:

``` bash
whoami
id
ls -l file
ls -ld directory
```

before changing permissions.

------------------------------------------------------------------------

# 15. `chown`

Changes ownership.

``` bash
sudo chown alice file.txt
```

Owner + group:

``` bash
sudo chown alice:developers file.txt
```

Recursive:

``` bash
sudo chown -R alice:developers /opt/myapp
```

### Production scenario

Your application runs as:

``` text
appuser
```

but files belong to:

``` text
root
```

The application cannot write.

Check:

``` bash
ls -l /opt/myapp
```

Then determine the correct ownership.

Don't blindly use recursive ownership changes on system directories.

------------------------------------------------------------------------

# 16. `chgrp`

Changes only the group.

``` bash
sudo chgrp developers file.txt
```

Useful when ownership should remain unchanged but a team needs shared
access.

------------------------------------------------------------------------

# 17. `umask`

`umask` is a **shell builtin command**.

Run:

``` bash
umask
```

You may see:

``` text
0022
```

or:

``` text
0002
```

### What does it do?

It controls permissions that are **removed from newly created
files/directories**.

Think:

``` text
Program creates file
       |
       v
Default permissions
       |
       v
umask removes permissions
       |
       v
Final permissions
```

Example:

``` bash
umask 0002
```

Typically results in:

``` text
New file       -> 664
New directory  -> 775
```

For files, Linux normally starts from:

``` text
666
```

and directories from:

``` text
777
```

With:

``` text
umask 002
```

the write bit for Others is removed.

``` text
File:

666
-  2
----
664
```

For directories:

``` text
777
-  2
----
775
```

### Why is this important?

Applications create files all the time:

``` text
logs
temporary files
uploads
reports
backups
```

`umask` determines how accessible those new files are by default.

------------------------------------------------------------------------

# 18. Processes --- What Are They?

A **process is a running instance of a program**.

For example:

``` text
Node.js program
       |
       v
Running Node.js process
       |
       v
PID = 1234
```

PID means:

> Process ID.

------------------------------------------------------------------------

# 19. `ps`

Shows processes.

``` bash
ps
```

More detailed:

``` bash
ps aux
```

### Production use

Find whether an application is running:

``` bash
ps aux | grep node
```

------------------------------------------------------------------------

# 20. `pgrep`

Find processes by name.

``` bash
pgrep -af node
```

This is often cleaner than:

``` bash
ps aux | grep node
```

because `pgrep` is specifically designed to locate processes.

------------------------------------------------------------------------

# 21. `pidof`

Find the PID of a program.

``` bash
pidof nginx
```

Example:

``` text
1234 1250
```

------------------------------------------------------------------------

# 22. `top`

Shows live system activity.

``` bash
top
```

You can see:

-   CPU usage
-   memory
-   processes
-   load
-   process IDs

### Production scenario

Users say:

> "The application is extremely slow."

Start:

``` bash
top
```

Maybe you discover:

``` text
node   95% CPU
```

Now investigate that process.

------------------------------------------------------------------------

# 23. `htop`

An interactive alternative to `top`.

``` bash
htop
```

It is easier to navigate visually if installed.

------------------------------------------------------------------------

# 24. `kill`

Sends a signal to a process.

``` bash
kill PID
```

For example:

``` bash
kill 1234
```

This normally requests graceful termination.

### Why not immediately use `kill -9`?

Because applications may need to:

-   close files
-   finish transactions
-   release resources
-   clean up temporary state

------------------------------------------------------------------------

# 25. `kill -9`

Forcefully terminates a process.

``` bash
kill -9 1234
```

Use as a last resort.

Think:

``` text
kill
  |
  v
"Please shut down."
  |
  v
Application gets opportunity to clean up
```

versus:

``` text
kill -9
  |
  v
"Stop immediately."
```

------------------------------------------------------------------------

# 26. `pkill`

Kills processes based on a name/pattern.

``` bash
pkill nginx
```

Be careful: it can affect multiple matching processes.

------------------------------------------------------------------------

# 27. Process Priority --- `nice`

Start a process with a different scheduling priority.

``` bash
nice -n 10 command
```

### Production idea

You have a CPU-heavy background job and don't want it to compete
aggressively with a critical application.

Use priority management carefully.

------------------------------------------------------------------------

# 28. `renice`

Changes priority of an existing process.

``` bash
renice 10 -p PID
```

Again, understand the workload before changing process priority.

------------------------------------------------------------------------

# 29. `systemctl`

`systemctl` controls **systemd services**.

Think:

``` text
systemd
   |
   +---- nginx
   +---- ssh
   +---- docker
   +---- your-app
```

Check:

``` bash
systemctl status nginx
```

Start:

``` bash
sudo systemctl start nginx
```

Stop:

``` bash
sudo systemctl stop nginx
```

Restart:

``` bash
sudo systemctl restart nginx
```

Reload:

``` bash
sudo systemctl reload nginx
```

Enable at boot:

``` bash
sudo systemctl enable nginx
```

Disable:

``` bash
sudo systemctl disable nginx
```

Check startup:

``` bash
systemctl is-enabled nginx
```

### Production flow

``` text
Website down
    |
    v
systemctl status nginx
    |
    +---- running? ----> investigate network/app
    |
    +---- failed? -----> journalctl
```

------------------------------------------------------------------------

# 30. Logs

Logs are the application's/server's record of what happened.

Common sources:

``` text
Application logs
System logs
Service logs
Kernel logs
Authentication logs
Web server logs
```

The production mindset is:

``` text
Something failed
      |
      v
Find relevant logs
      |
      v
Search for ERROR/WARN
      |
      v
Find timestamp
      |
      v
Correlate with deployment/change
      |
      v
Find root cause
```

------------------------------------------------------------------------

# 31. `journalctl`

Reads the systemd journal.

All journal entries:

``` bash
journalctl
```

Go to the end:

``` bash
journalctl -e
```

Follow live:

``` bash
journalctl -f
```

Specific service:

``` bash
journalctl -u nginx
```

Follow service:

``` bash
journalctl -u nginx -f
```

Today's logs:

``` bash
journalctl --since today
```

Last hour:

``` bash
journalctl --since "1 hour ago"
```

Current boot:

``` bash
journalctl -b
```

Previous boot:

``` bash
journalctl -b -1
```

### Production example

Service failed:

``` bash
systemctl status myapp
```

Then:

``` bash
journalctl -u myapp --since "30 minutes ago"
```

------------------------------------------------------------------------

# 32. `logrotate`

Logs can grow forever.

Imagine:

``` text
application.log
      |
      v
100 MB
      |
      v
1 GB
      |
      v
10 GB
      |
      v
50 GB
      |
      v
DISK FULL
```

`logrotate` helps manage this.

Configuration:

``` bash
/etc/logrotate.conf
```

Service-specific rules:

``` bash
/etc/logrotate.d/
```

Test:

``` bash
sudo logrotate -d /etc/logrotate.conf
```

Force:

``` bash
sudo logrotate -f /etc/logrotate.conf
```

Never force rotation blindly on production without understanding the
configuration.

------------------------------------------------------------------------

# 33. Disk Troubleshooting

## `df`

Shows filesystem capacity.

``` bash
df
```

Human-readable:

``` bash
df -h
```

### Production scenario

Application suddenly stops writing files.

Run:

``` bash
df -h
```

You may find:

``` text
/dev/sda1   100%
```

The filesystem is full.

------------------------------------------------------------------------

## `df -i`

Checks inode usage.

``` bash
df -ih
```

Why does this matter?

You can have free disk space but still be unable to create files if all
inodes are consumed.

``` text
Disk space: 30% used
Inodes:     100% used
```

The system can still fail to create new files.

------------------------------------------------------------------------

# 34. `du`

Shows how much space files/directories use.

``` bash
du
```

Human-readable:

``` bash
du -h
```

Summary:

``` bash
du -sh /var/log
```

Find large areas:

``` bash
sudo du -xh /var | sort -h | tail
```

### Flow

``` text
df -h
   |
   v
/var is nearly full
   |
   v
du -sh /var/*
   |
   v
/var/log is huge
   |
   v
du -sh /var/log/*
   |
   v
application.log is huge
   |
   v
Investigate log rotation
```

------------------------------------------------------------------------

# 35. `lsblk`

Shows block devices/disks.

``` bash
lsblk
```

Useful when investigating:

-   disks
-   partitions
-   mount relationships

------------------------------------------------------------------------

# 36. `mount`

Shows or mounts filesystems.

``` bash
mount
```

Mount a device:

``` bash
sudo mount /dev/sdb1 /mnt/data
```

### Warning

Storage commands can cause serious problems if you use the wrong device.

Always verify with:

``` bash
lsblk
blkid
findmnt
```

before making changes.

------------------------------------------------------------------------

# 37. `umount`

Unmounts a filesystem.

``` bash
sudo umount /mnt/data
```

Do not simply unmount production storage because a command tells you to.
First understand what uses it.

------------------------------------------------------------------------

# 38. `findmnt`

Shows mounted filesystems clearly.

``` bash
findmnt
```

Useful when you need to answer:

> "Where is this disk mounted?"

------------------------------------------------------------------------

# 39. `blkid`

Shows block-device metadata such as UUID and filesystem type.

``` bash
sudo blkid
```

Useful when configuring persistent mounts.

------------------------------------------------------------------------

# 40. User Management

## `id`

Shows user identity and groups.

``` bash
id
```

For another user:

``` bash
id alice
```

Example:

``` text
uid=1001(alice) gid=1001(alice) groups=1001(alice),2000(developers)
```

This is extremely useful when debugging permissions.

------------------------------------------------------------------------

## `who`

Shows logged-in users.

``` bash
who
```

------------------------------------------------------------------------

## `w`

Shows logged-in users plus activity.

``` bash
w
```

------------------------------------------------------------------------

## `last`

Shows login history.

``` bash
last
```

Useful when investigating:

> "Who logged into this server recently?"

------------------------------------------------------------------------

# 41. Creating Users

## `useradd`

Creates a user.

``` bash
sudo useradd -m alice
```

`-m` creates the home directory.

Set password:

``` bash
sudo passwd alice
```

Modify:

``` bash
sudo usermod ...
```

Delete:

``` bash
sudo userdel alice
```

Password aging:

``` bash
sudo chage ...
```

List/query users:

``` bash
getent passwd
```

### Production access model

``` text
Employee
   |
   v
Linux user
   |
   v
Group membership
   |
   v
Permissions
   |
   v
Sudo rules
```

------------------------------------------------------------------------

# 42. Groups

A group is a way of giving the same access to multiple users.

Create:

``` bash
sudo groupadd developers
```

Check:

``` bash
getent group developers
```

Add user:

``` bash
sudo usermod -aG developers alice
```

The `-aG` is important:

``` text
-a = append
-G = supplementary group
```

Remove:

``` bash
sudo gpasswd -d alice developers
```

Check:

``` bash
groups alice
```

------------------------------------------------------------------------

# 43. Why Groups Matter in Production

Instead of:

``` text
Alice -> manually grant permission
Bob   -> manually grant permission
Carol -> manually grant permission
```

use:

``` text
developers
    |
    +---- Alice
    +---- Bob
    +---- Carol
```

Then give the group access.

This makes access control easier to manage.

------------------------------------------------------------------------

# 44. `sudo`

`sudo` means:

> Run a command with elevated privileges, subject to the user's sudo
> policy.

Example:

``` bash
sudo systemctl restart nginx
```

You don't need to become root permanently.

------------------------------------------------------------------------

# 45. `sudo -l`

Shows what commands your current user can run with sudo.

``` bash
sudo -l
```

For another user:

``` bash
sudo -l -U alice
```

This is useful when troubleshooting:

> "Why can this person restart the application but not install
> packages?"

------------------------------------------------------------------------

# 46. `visudo`

Safely edits sudo configuration.

``` bash
sudo visudo
```

Or a dedicated file:

``` bash
sudo visudo -f /etc/sudoers.d/developers
```

Validate:

``` bash
sudo visudo -c
```

### Why use `visudo`?

A syntax error in sudo configuration can lock administrators out of
privileged access.

------------------------------------------------------------------------

# 47. Principle of Least Privilege

Don't give:

``` text
developers -> everything
```

when they only need:

``` text
developers
    |
    +-- systemctl status myapp
    +-- systemctl restart myapp
    +-- journalctl -u myapp
```

Production security principle:

> Give users only the permissions they actually need.

------------------------------------------------------------------------

# 48. Package Management --- Debian/Ubuntu

## `apt update`

Updates the local package metadata.

``` bash
sudo apt update
```

Important:

> `apt update` does not mean "upgrade all software."

It refreshes information about available packages.

------------------------------------------------------------------------

## `apt upgrade`

Installs available package upgrades.

``` bash
sudo apt upgrade
```

------------------------------------------------------------------------

## `apt install`

Installs software.

``` bash
sudo apt install nginx
```

------------------------------------------------------------------------

## `apt remove`

Removes a package.

``` bash
sudo apt remove nginx
```

------------------------------------------------------------------------

## `apt search`

Searches packages.

``` bash
apt search nginx
```

------------------------------------------------------------------------

## `apt show`

Shows package information.

``` bash
apt show nginx
```

------------------------------------------------------------------------

## `apt list --installed`

Lists installed packages.

``` bash
apt list --installed
```

------------------------------------------------------------------------

## `dpkg`

Low-level Debian package management.

``` bash
dpkg -l | grep nginx
```

Useful when checking whether a specific Debian package is installed.

------------------------------------------------------------------------

# 49. RHEL/CentOS/Fedora --- `dnf` / `yum`

Modern systems commonly use:

``` bash
dnf
```

Install:

``` bash
sudo dnf install nginx
```

Update:

``` bash
sudo dnf update
```

Search:

``` bash
dnf search nginx
```

Remove:

``` bash
sudo dnf remove nginx
```

Installed:

``` bash
dnf list installed
```

RPM package query:

``` bash
rpm -qa | grep nginx
```

Older environments may use:

``` bash
yum
```

------------------------------------------------------------------------

# 50. SSH

SSH is how you securely connect to a remote Linux server.

``` bash
ssh user@server
```

Conceptually:

``` text
Your laptop
     |
     | SSH
     v
Linux production server
```

Specify key:

``` bash
ssh -i ~/.ssh/id_rsa user@server
```

Specify port:

``` bash
ssh -p 2222 user@server
```

Verbose troubleshooting:

``` bash
ssh -v user@server
ssh -vvv user@server
```

------------------------------------------------------------------------

# 51. SSH Keys

Generate:

``` bash
ssh-keygen
```

Concept:

``` text
Private key
   |
   | stays with you
   v
Your laptop

Public key
   |
   | copied to server
   v
Server ~/.ssh/authorized_keys
```

Copy public key:

``` bash
ssh-copy-id user@server
```

Then:

``` bash
ssh user@server
```

Common permissions:

``` bash
chmod 700 ~/.ssh
chmod 600 ~/.ssh/id_rsa
chmod 644 ~/.ssh/id_rsa.pub
chmod 600 ~/.ssh/authorized_keys
```

Never share your private key.

------------------------------------------------------------------------

# 52. `scp`

Securely copies files over SSH.

``` bash
scp file.txt user@server:/tmp/
```

Directory:

``` bash
scp -r myapp user@server:/opt/
```

------------------------------------------------------------------------

# 53. `rsync`

Efficiently synchronizes files/directories.

``` bash
rsync -avz ./app/ user@server:/opt/app/
```

Why use it?

Unlike blindly copying everything, `rsync` can compare files and
transfer only what needs transferring.

Very useful for:

-   deployments
-   backups
-   synchronization

------------------------------------------------------------------------

# 54. Cron

Cron schedules commands.

Edit:

``` bash
crontab -e
```

List:

``` bash
crontab -l
```

Basic format:

``` text
MINUTE HOUR DAY MONTH DAY_OF_WEEK COMMAND
```

Example:

``` text
0 2 * * * /opt/scripts/backup.sh
```

Means:

> Run `/opt/scripts/backup.sh` every day at 2:00 AM.

Every 5 minutes:

``` text
*/5 * * * * /opt/scripts/health-check.sh
```

Sunday at 3 AM:

``` text
0 3 * * 0 /opt/scripts/weekly-backup.sh
```

### Production flow

``` text
Cron
 |
 | checks time
 v
Schedule matches
 |
 v
Run script
 |
 v
Script succeeds/fails
 |
 v
Write log
```

Always give production cron jobs explicit paths and logging.

------------------------------------------------------------------------

# 55. Shell Scripting

Shell scripts automate repetitive work.

Start:

``` bash
#!/bin/bash
```

Variable:

``` bash
NAME="production"
echo "$NAME"
```

Condition:

``` bash
if [ -f "/tmp/test.txt" ]; then
    echo "File exists"
else
    echo "File does not exist"
fi
```

Loop:

``` bash
for server in server1 server2 server3
do
    echo "$server"
done
```

Command substitution:

``` bash
DATE=$(date)
echo "$DATE"
```

Arguments:

``` bash
echo "Server: $1"
```

Run:

``` bash
./backup.sh
```

or:

``` bash
bash backup.sh
```

------------------------------------------------------------------------

# 56. Script Exit Codes

After a command:

``` bash
echo $?
```

Usually:

``` text
0     = success
non-0 = failure
```

Example:

``` bash
if systemctl is-active --quiet nginx; then
    echo "Running"
else
    echo "Not running"
fi
```

This is important because automation needs to know whether something
succeeded.

------------------------------------------------------------------------

# 57. Bash Debugging

Run a script with tracing:

``` bash
bash -x backup.sh
```

Inside a script:

``` bash
set -x
```

A common strict-mode pattern:

``` bash
set -euo pipefail
```

Learn what each option means before putting it into production scripts.

------------------------------------------------------------------------

# 58. Environment Variables

Show environment:

``` bash
env
```

or:

``` bash
printenv
```

Specific variable:

``` bash
echo "$PATH"
```

### Production scenario

Manual command works:

``` text
./deploy.sh
```

Cron fails:

``` text
cron -> deploy.sh -> command not found
```

Potential cause:

> Cron has a different environment/PATH.

Investigate:

``` bash
echo "$PATH"
env
```

------------------------------------------------------------------------

# 59. System Information

Kernel:

``` bash
uname -a
```

OS/hostname:

``` bash
hostnamectl
```

Uptime/load:

``` bash
uptime
```

Memory:

``` bash
free -h
```

CPU:

``` bash
lscpu
```

Storage:

``` bash
lsblk
```

Filesystem:

``` bash
df -h
```

Time:

``` bash
date
```

Time configuration:

``` bash
timedatectl
```

These commands give you a quick server health snapshot.

------------------------------------------------------------------------

# 60. `lsof`

Means:

> List Open Files.

Linux treats many resources like files, so `lsof` can tell you which
process is using something.

``` bash
lsof
```

Specific file:

``` bash
sudo lsof /var/log/application.log
```

Specific port:

``` bash
sudo lsof -i :8080
```

### Production scenario

You receive:

``` text
Port 8080 is already in use
```

Run:

``` bash
sudo lsof -i :8080
```

Now you can identify the process.

------------------------------------------------------------------------

# 61. `vmstat`

Shows system activity.

``` bash
vmstat 1
```

Useful for observing:

-   processes
-   memory
-   paging
-   CPU
-   I/O

Use it when the problem is more complicated than just "CPU is high."

------------------------------------------------------------------------

# 62. `iostat`

Useful for disk I/O.

``` bash
iostat
```

Detailed continuous monitoring:

``` bash
iostat -xz 1
```

Production scenario:

``` text
Application slow
      |
      v
CPU looks normal
      |
      v
RAM looks normal
      |
      v
Could disk I/O be slow?
      |
      v
iostat -xz 1
```

------------------------------------------------------------------------

# 63. `dmesg`

Shows kernel messages.

``` bash
dmesg
```

Search errors:

``` bash
dmesg | grep -i error
```

Search OOM:

``` bash
dmesg | grep -i oom
```

Useful for:

-   kernel issues
-   hardware/device issues
-   filesystem problems
-   memory pressure/OOM events

------------------------------------------------------------------------

# 64. Zombie Processes

A zombie is a process that has **already finished executing** but whose
parent hasn't collected its exit status.

``` text
Parent
  |
  +---- Child
          |
          v
       exits
          |
          v
       ZOMBIE
          |
          v
Parent collects status
          |
          v
       removed
```

Find zombies:

``` bash
ps -eo pid,ppid,stat,cmd | grep Z
```

### Important

You generally don't solve a zombie by simply killing the zombie.

It is already dead.

Investigate the **parent process**.

------------------------------------------------------------------------

# 65. `history`

Shows command history.

``` bash
history
```

Search:

``` bash
history | grep systemctl
```

Useful when you remember:

> "I ran some command earlier but forgot exactly what it was."

Don't treat shell history as a complete security/audit record.

------------------------------------------------------------------------

# 66. `watch`

Repeats a command.

``` bash
watch df -h
```

Another example:

``` bash
watch "ss -lntp"
```

Useful when monitoring something changing in real time.

------------------------------------------------------------------------

# 67. `awk`

`awk` is a powerful text-processing tool.

Simple example:

``` bash
awk '{print $1}' access.log
```

It prints the first whitespace-separated field.

Count repeated values:

``` bash
awk '{print $1}' access.log | sort | uniq -c
```

### Production example

You want to find the most common IP addresses in an access log.

``` text
access.log
   |
   v
awk -> extract IP
   |
   v
sort
   |
   v
uniq -c
   |
   v
count requests
```

------------------------------------------------------------------------

# 68. `sed`

Text-processing/editing tool.

Replace text:

``` bash
sed 's/old/new/g' file.txt
```

Display lines:

``` bash
sed -n '1,20p' application.log
```

Useful in scripts and automation.

Be careful with in-place edits:

``` bash
sed -i ...
```

because it modifies the actual file.

------------------------------------------------------------------------

# 69. `xargs`

Takes output from one command and turns it into arguments for another.

Example:

``` bash
find /tmp -name "*.tmp" -print0 | xargs -0 ls -lh
```

This can be very powerful.

It can also be dangerous if combined with destructive commands.

Understand the final command before executing it.

------------------------------------------------------------------------

# 70. Archives --- `tar`

Create archive:

``` bash
tar -czf backup.tar.gz /opt/myapp
```

Extract:

``` bash
tar -xzf backup.tar.gz
```

Inspect without extracting:

``` bash
tar -tzf backup.tar.gz
```

Useful for:

-   backups
-   deployments
-   transferring application bundles
-   log archives

------------------------------------------------------------------------

# 71. `gzip` / `gunzip`

Compress:

``` bash
gzip file.log
```

Decompress:

``` bash
gunzip file.log.gz
```

Search compressed log:

``` bash
zgrep "ERROR" application.log.gz
```

Very useful when old rotated logs are compressed.

------------------------------------------------------------------------

# 72. Networking Mental Model

When an application connects to another service:

``` text
Application
    |
    v
DNS
    |
    v
IP address
    |
    v
Routing
    |
    v
Firewall/security rules
    |
    v
TCP/UDP port
    |
    v
Destination service
```

If you troubleshoot in this order, networking becomes much easier.

------------------------------------------------------------------------

# 73. `ip addr`

Shows IP addresses and network interfaces.

``` bash
ip addr
```

### Production scenario

Check whether the server has the expected IP address or whether a
network interface is up.

------------------------------------------------------------------------

# 74. `ip link`

Shows network interfaces and their state.

``` bash
ip link
```

Useful when checking whether an interface is up/down.

------------------------------------------------------------------------

# 75. `ip route`

Shows routing table.

``` bash
ip route
```

You can identify the default gateway and routes to networks.

### Production scenario

Server can reach some networks but not another.

Start with:

``` bash
ip route
```

------------------------------------------------------------------------

# 76. `ping`

Tests basic network reachability using ICMP.

``` bash
ping <host>
```

Example:

``` bash
ping 10.20.5.10
```

### Important

A failed ping does **not always mean the server is unreachable**.
Firewalls may block ICMP.

Use it as one piece of evidence.

------------------------------------------------------------------------

# 77. `ss`

Shows network sockets.

Listening TCP ports:

``` bash
sudo ss -lntp
```

This is one of the most useful commands for:

> "Is my application actually listening on the expected port?"

Example:

``` text
Application should listen on 8080
             |
             v
sudo ss -lntp
             |
             v
Is 8080 present?
```

------------------------------------------------------------------------

# 78. `curl`

Tests HTTP/API behavior.

Basic:

``` bash
curl http://localhost:8080
```

Verbose:

``` bash
curl -v http://localhost:8080
```

Headers:

``` bash
curl -I https://example.com
```

POST JSON:

``` bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"name":"test"}' \
  http://localhost:8080/api
```

### Production scenario

You think an API server is broken.

Instead of immediately blaming the load balancer:

``` bash
curl -v http://localhost:8080/health
```

If localhost works but external traffic fails, investigate the
network/load balancer path.

------------------------------------------------------------------------

# 79. DNS --- `dig`

DNS translates names into IP addresses.

``` text
api.company.com
       |
       v
      DNS
       |
       v
10.20.30.40
```

Run:

``` bash
dig example.com
```

Specific record:

``` bash
dig example.com A
```

Name servers:

``` bash
dig example.com NS
```

### Production scenario

Users report:

> "api.company.com isn't working."

Check:

``` bash
dig api.company.com
```

------------------------------------------------------------------------

# 80. `nslookup`

Another DNS lookup tool.

``` bash
nslookup example.com
```

It is useful for basic DNS troubleshooting.

------------------------------------------------------------------------

# 81. `/etc/resolv.conf`

Shows DNS resolver configuration on many Linux systems.

``` bash
cat /etc/resolv.conf
```

Do not blindly edit this file on modern systems because DNS
configuration may be managed by NetworkManager, systemd-resolved,
cloud-init, or another network manager.

------------------------------------------------------------------------

# 82. `nc` --- Netcat

Tests TCP/UDP connectivity.

``` bash
nc -vz 10.20.5.10 5432
```

Think:

``` text
Application server
       |
       | TCP 5432
       v
Database server
```

If it fails, investigate:

-   route
-   firewall
-   security rules
-   destination service
-   service binding

------------------------------------------------------------------------

# 83. `traceroute`

Shows the network path toward a destination.

``` bash
traceroute example.com
```

Useful when you suspect traffic is failing somewhere along the path.

------------------------------------------------------------------------

# 84. `tcpdump`

Captures network packets.

Basic:

``` bash
sudo tcpdump
```

Port:

``` bash
sudo tcpdump port 443
```

Host:

``` bash
sudo tcpdump host 10.20.5.10
```

Interface:

``` bash
sudo tcpdump -i eth0
```

### When do I use it?

Only after simpler tests haven't explained the problem.

``` text
curl
  |
  v
ss
  |
  v
nc
  |
  v
route/firewall investigation
  |
  v
tcpdump
```

Packet captures can contain sensitive information and can be large.
Follow your organization's security/change procedures.

------------------------------------------------------------------------

# 85. `strace`

Shows system calls made by a process.

``` bash
sudo strace -p PID
```

You might see calls such as:

``` text
open()
read()
write()
connect()
stat()
```

### Why is this advanced?

Application logs may say:

``` text
Something failed
```

but `strace` can show:

``` text
connect() -> connection refused
open() -> permission denied
```

Use carefully because tracing can add overhead.

------------------------------------------------------------------------

# 86. systemd Timers

Cron is not the only scheduling mechanism.

List timers:

``` bash
systemctl list-timers
```

All:

``` bash
systemctl list-timers --all
```

Production environments may use:

``` text
cron
or
systemd timers
```

So know both.

------------------------------------------------------------------------

# 87. Common Production Incident: Application Down

Use this sequence:

``` bash
hostname
whoami
systemctl status myapp
journalctl -u myapp --since "30 minutes ago"
pgrep -af myapp
ss -lntp
top
free -h
df -h
tail -100 /path/to/app.log
curl -v http://localhost:8080/health
```

### Why this order?

``` text
Correct server?
      |
      v
Service running?
      |
      v
Why did it fail?
      |
      v
Process exists?
      |
      v
Port listening?
      |
      v
CPU/RAM okay?
      |
      v
Disk okay?
      |
      v
Application log?
      |
      v
Does application respond locally?
```

------------------------------------------------------------------------

# 88. Common Production Incident: Disk Full

Start:

``` bash
df -h
```

Then:

``` bash
df -ih
```

Then:

``` bash
sudo du -xh /var | sort -h | tail
```

Then:

``` bash
sudo du -sh /var/log/*
```

Then:

``` bash
sudo find /var -type f -size +1G -exec ls -lh {} \;
```

Do not immediately delete things.

Ask:

> Why did this file grow?

Possible causes:

-   log rotation failure
-   application error loop
-   temporary files
-   backups
-   database data
-   core dumps

------------------------------------------------------------------------

# 89. Common Production Incident: Permission Denied

Start:

``` bash
whoami
id
ls -l /path/to/file
ls -ld /path/to/directory
```

Then, if needed:

``` bash
namei -l /path/to/file
getfacl /path/to/file
```

### Why `namei`?

Suppose:

``` text
/opt/app/config/settings.conf
```

The file itself might be readable, but the user may not have
execute/search permission on one of the parent directories.

`namei -l` helps inspect every path component.

### Why `getfacl`?

Traditional `ls -l` doesn't always tell the whole permission story. ACLs
can provide additional access rules.

------------------------------------------------------------------------

# 90. Common Production Incident: Connection Refused

Run:

``` bash
ss -lntp
```

Then:

``` bash
systemctl status <service>
```

Then:

``` bash
journalctl -u <service>
```

Then:

``` bash
nc -vz <host> <port>
```

Then:

``` bash
curl -v http://<host>:<port>
```

"Connection refused" generally means the network path reached the
destination but the destination did not accept the connection on that
port.

Possible causes:

-   application stopped
-   wrong port
-   service bound only to localhost
-   firewall/security configuration
-   incorrect service configuration

------------------------------------------------------------------------

# 91. Common Production Incident: Connection Timeout

Start:

``` bash
ip route
```

Then:

``` bash
ping <host>
```

Then:

``` bash
nc -vz <host> <port>
```

Then:

``` bash
traceroute <host>
```

If necessary:

``` bash
sudo tcpdump
```

A timeout can involve:

``` text
Routing
Firewall
Security group
Network ACL
Dropped packets
Service/network path
```

------------------------------------------------------------------------

# 92. Common Production Incident: High CPU

Start:

``` bash
top
```

Then:

``` bash
ps aux --sort=-%cpu | head
```

Then identify the process:

``` bash
pgrep -af <process>
```

Then inspect its logs:

``` bash
journalctl -u <service>
```

Don't automatically kill the process. First understand why it is
consuming CPU.

------------------------------------------------------------------------

# 93. Common Production Incident: High Memory

Start:

``` bash
free -h
```

Then:

``` bash
ps aux --sort=-%mem | head
```

Then:

``` bash
top
```

Then investigate the responsible service and logs.

------------------------------------------------------------------------

# 94. Common Production Incident: Disk I/O

Start:

``` bash
iostat -xz 1
```

Then:

``` bash
vmstat 1
```

Then:

``` bash
top
```

The question is:

> Is the application slow because the CPU is busy, or because storage is
> slow?

------------------------------------------------------------------------

# 95. Zombie Process Investigation

Find zombies:

``` bash
ps -eo pid,ppid,stat,cmd | grep Z
```

Example:

``` text
PID   PPID   STAT
500   100    Z
```

The important value is:

``` text
PPID = 100
```

That is the parent.

Investigate:

``` bash
ps -fp 100
```

The zombie itself is already dead.

The real investigation is:

``` text
Zombie
  |
  v
Find PPID
  |
  v
Inspect parent
  |
  v
Why isn't parent collecting children?
```

------------------------------------------------------------------------

# 96. Useful Advanced Commands

## `uptime`

``` bash
uptime
```

Shows uptime and load averages.

------------------------------------------------------------------------

## `free`

``` bash
free -h
```

Shows memory and swap information.

------------------------------------------------------------------------

## `lscpu`

``` bash
lscpu
```

Shows CPU architecture and CPU details.

------------------------------------------------------------------------

## `vmstat`

``` bash
vmstat 1
```

Shows memory/process/CPU/I/O activity over time.

------------------------------------------------------------------------

## `iostat`

``` bash
iostat -xz 1
```

Useful for storage performance.

------------------------------------------------------------------------

## `dmesg`

``` bash
dmesg | grep -i error
```

Useful for kernel-level errors.

------------------------------------------------------------------------

## `lsof`

``` bash
sudo lsof -i :8080
```

Find which process uses a port.

------------------------------------------------------------------------

## `watch`

``` bash
watch df -h
```

Continuously monitor a command.

------------------------------------------------------------------------

# 97. Networking Command Set

## Server network identity

``` bash
hostname
hostname -I
ip addr
ip link
```

## Routing

``` bash
ip route
```

## Basic connectivity

``` bash
ping <host>
```

## Ports

``` bash
ss -lntp
sudo lsof -i :8080
nc -vz <host> <port>
```

## HTTP

``` bash
curl
curl -v
curl -I
```

## DNS

``` bash
dig
nslookup
cat /etc/resolv.conf
```

## Path

``` bash
traceroute
```

## Deep packet investigation

``` bash
tcpdump
```

------------------------------------------------------------------------

# 98. Command Combination Skills

The real Linux skill is not knowing isolated commands.

It's combining them.

Example:

``` bash
ps aux | grep nginx
```

Example:

``` bash
journalctl -u nginx | grep -i error
```

Example:

``` bash
ps aux --sort=-%cpu | head
```

Example:

``` bash
du -xh /var | sort -h | tail
```

Example:

``` bash
awk '{print $1}' access.log | sort | uniq -c
```

The pattern is:

``` text
Command A
   |
   v
produce useful information
   |
   v
Command B
   |
   v
filter/transform it
   |
   v
Actionable information
```

------------------------------------------------------------------------

# 99. Production Command Cheat Sheet

  Problem                  First commands
  ------------------------ ---------------------------------------------
  Where am I?              `pwd`, `hostname`, `whoami`
  What files exist?        `ls -lah`
  Find a file              `find`, `locate`
  Read a config            `less`, `cat`
  Watch logs               `tail -f`
  Search logs              `grep`
  Service down             `systemctl status`, `journalctl -u`
  High CPU                 `top`, `ps aux --sort=-%cpu`
  High RAM                 `free -h`, `ps aux --sort=-%mem`
  Disk full                `df -h`, `du`, `find`
  Inodes full              `df -ih`
  Permission denied        `whoami`, `id`, `ls -l`, `namei`, `getfacl`
  Port unavailable         `ss`, `lsof`, `nc`
  HTTP problem             `curl -v`
  DNS problem              `dig`, `nslookup`
  Routing problem          `ip route`, `traceroute`
  Network deep dive        `tcpdump`
  Process not responding   `ps`, `top`, `kill`
  Zombie process           `ps -eo ...` + inspect PPID
  Disk I/O problem         `iostat`, `vmstat`
  Kernel issue             `dmesg`
  SSH problem              `ssh -v` / `ssh -vvv`
  Scheduled job            `crontab -l`, `systemctl list-timers`
  Package issue            `apt` / `dnf` / `rpm` / `dpkg`

------------------------------------------------------------------------

# 100. The Production Troubleshooting Flow to Memorize

Don't memorize every command first. Memorize this:

``` text
                    INCIDENT
                       |
                       v
                1. IDENTIFY
             hostname / whoami
                       |
                       v
                2. OBSERVE
          top / free / df / ps
                       |
                       v
                3. CHECK SERVICE
            systemctl status
                       |
                       v
                4. CHECK LOGS
         journalctl / tail / grep
                       |
                       v
                5. CHECK NETWORK
       ip / ss / curl / dig / nc
                       |
                       v
                6. GO DEEPER
       lsof / iostat / tcpdump
                / strace / dmesg
                       |
                       v
                 7. DIAGNOSE
                       |
                       v
                    8. FIX
                       |
                       v
                  9. VERIFY
                       |
                       v
                 10. MONITOR
```

## Example

Manager:

> "The production API is down."

You should not panic and immediately restart it.

Think:

``` bash
hostname
whoami

systemctl status myapp

journalctl -u myapp --since "30 minutes ago"

pgrep -af myapp

ss -lntp

df -h
free -h

tail -100 /path/to/application.log

curl -v http://localhost:8080/health
```

If the application isn't listening:

``` text
ss
  |
  v
No 8080
  |
  v
systemctl
  |
  v
Service failed
  |
  v
journalctl
  |
  v
Find error
```

If it is listening but users cannot reach it:

``` text
ss -> port exists
       |
       v
curl localhost -> works
       |
       v
External request -> fails
       |
       v
Investigate DNS/routing/firewall/load balancer
```

That is how Linux commands become **production troubleshooting skills**,
rather than a list of commands to memorize.

------------------------------------------------------------------------

# 101. Production Safety Rules

Before destructive or high-impact commands:

``` bash
hostname
whoami
pwd
```

Then inspect.

Be especially careful with:

``` bash
rm -rf
chmod -R
chown -R
kill -9
systemctl stop
systemctl restart
mount
umount
```

Never use:

``` bash
chmod 777
```

as your default solution to permission problems.

Never delete logs simply because they are large without understanding
why they became large.

Never expose:

-   passwords
-   private SSH keys
-   API secrets
-   database credentials
-   production tokens

And always follow your organization's:

-   change-management process
-   access-control policy
-   backup policy
-   incident-response procedure

------------------------------------------------------------------------

# 102. Recommended Hands-On Order

Practice on a VM, lab server, or other safe environment:

``` text
Filesystem navigation
        ↓
Files/directories
        ↓
Permissions
        ↓
Ownership
        ↓
umask
        ↓
Users/groups
        ↓
sudo
        ↓
Processes
        ↓
systemctl
        ↓
journalctl
        ↓
Disk troubleshooting
        ↓
Shell scripting
        ↓
Cron
        ↓
SSH
        ↓
Networking
        ↓
CPU/RAM/I/O
        ↓
Advanced troubleshooting
```

The goal is not:

> "I know what `ps` means."

The goal is:

> "I can look at a production symptom, choose the correct Linux layer,
> run the right commands, interpret the output, identify the likely root
> cause, make a controlled change, and verify that the system
> recovered."

------------------------------------------------------------------------

# 103. Final Quick Reference

## Files

``` bash
pwd
ls
ls -l
ls -la
ls -lh
ls -lah
cd
cd ..
cd ~
tree
file
stat
find
locate
which
whereis
touch
mkdir
mkdir -p
cp
cp -r
mv
rm
rm -r
rm -rf
cat
less
more
head
tail
tail -f
wc
nl
cut
sort
uniq
grep
```

## Permissions

``` bash
chmod
chown
chgrp
umask
getfacl
namei
```

## Processes

``` bash
ps
ps aux
top
htop
pgrep
pidof
kill
kill -9
pkill
nice
renice
```

## Services/logs

``` bash
systemctl
journalctl
logrotate
```

## Disk

``` bash
df
df -h
df -i
df -ih
du
du -h
du -sh
lsblk
mount
umount
findmnt
blkid
```

## Users/groups/sudo

``` bash
whoami
id
who
w
last
useradd
passwd
usermod
userdel
chage
getent
groupadd
groups
gpasswd
sudo
sudo -l
visudo
```

## Packages

``` bash
apt
apt update
apt upgrade
apt install
apt remove
apt search
apt show
apt list --installed
dpkg
dnf
yum
rpm
```

## SSH

``` bash
ssh
ssh -i
ssh -p
ssh -v
ssh -vvv
ssh-keygen
ssh-copy-id
scp
rsync
```

## Automation

``` bash
crontab
bash
echo
set -x
set -euo pipefail
systemctl list-timers
```

## System/performance

``` bash
uname
hostnamectl
uptime
free
lscpu
date
timedatectl
lsof
vmstat
iostat
dmesg
watch
history
```

## Networking

``` bash
ip addr
ip link
ip route
ping
ss
curl
dig
nslookup
cat /etc/resolv.conf
nc
traceroute
tcpdump
strace
```

## Text/automation

``` bash
awk
sed
xargs
tar
gzip
gunzip
zgrep
```

------------------------------------------------------------------------

**Remember:** Production Linux is not about knowing the longest list of
commands. It is about knowing **what question you are trying to
answer**, selecting the command that answers that question, and
interpreting the result correctly.
