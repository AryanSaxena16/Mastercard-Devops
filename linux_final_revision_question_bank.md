# Linux Final Revision Question Bank

Use this as your final Linux revision file.

Question-based syllabus coverage in this file: 50/50 questions = 100%.

How to use:

1. Hide the "Correct answer" section.
2. Write your answer under "Your answer".
3. Check the correct answer.
4. Focus on the "Meaning" and "Why we use it" sections.

Your current answered-and-reviewed coverage before this file: 18/50 = 36/100.

---

## Filesystem And Navigation

### Question 1: What command shows your current directory?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
pwd
```

Meaning:

```text
pwd = print working directory
```

Why we use it:

```text
Before changing or deleting files
-> confirm where you are
-> avoid running commands in the wrong directory
```

---

### Question 2: What command lists all files, including hidden files, with permissions?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ls -la
```

Meaning:

```text
ls = list files
-l = long format with permissions, owner, group, size, time
-a = show hidden files too
```

Why we use it:

```text
Need to inspect a directory
-> check files
-> check hidden config files
-> check permissions and ownership
```

---

### Question 3: What command searches for all `.log` files under `/var/log`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
find /var/log -name "*.log"
```

Meaning:

```text
find /var/log = search under /var/log
-name "*.log" = match files ending with .log
```

Why we use it:

```text
Need to locate log files
-> search a directory tree
-> identify files to inspect
```

Safer production version:

```bash
sudo find /var/log -type f -name "*.log"
```

---

### Question 4: What command creates a symbolic link named `linkname` pointing to `target`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ln -s target linkname
```

Meaning:

```text
ln = create link
-s = symbolic link
target = original file or directory
linkname = shortcut name
```

Why we use it:

```text
Need one path to point to another
-> create symlink
-> avoid copying duplicate files
```

---

## Permissions And Ownership

### Question 5: What does `chmod 640 file.txt` mean?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
chmod 640 file.txt
```

Meaning:

```text
6 = rw- for owner
4 = r-- for group
0 = --- for others
```

So:

```text
owner  = read and write
group  = read only
others = no access
```

Why we use it:

```text
Need controlled file access
-> owner can edit
-> group can read
-> everyone else blocked
```

---

### Question 6: What does `chmod 755 script.sh` mean?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
chmod 755 script.sh
```

Meaning:

```text
7 = rwx for owner
5 = r-x for group
5 = r-x for others
```

So:

```text
owner  = read, write, execute
group  = read, execute
others = read, execute
```

Why we use it:

```text
Need a script to be runnable
-> owner can edit and run it
-> others can run it
-> others cannot modify it
```

---

### Question 7: What command changes the owner and group of `app.log` to `appuser:appgroup`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo chown appuser:appgroup app.log
```

Meaning:

```text
chown = change ownership
appuser = new owner
appgroup = new group
app.log = target file
```

Why we use it:

```text
App cannot access a file
-> check ownership
-> if ownership is wrong, change it
-> verify app can read or write
```

Do not use `chown -R` unless you have verified the exact directory.

---

### Question 8: Why is `chmod 777 file` dangerous?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
chmod 777 file
```

Meaning:

```text
owner  = read, write, execute
group  = read, write, execute
others = read, write, execute
```

Why it is dangerous:

```text
Everyone can modify or execute the file
-> accidental changes become easier
-> malicious changes become easier
-> least privilege is broken
```

Better flow:

```text
Permission denied
-> check user, group, owner, parent directory
-> apply the smallest safe permission change
```

---

### Question 9: What command shows the permissions of a parent directory itself, not its contents?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ls -ld /path/to/directory
```

Meaning:

```text
ls = list
-l = long format
-d = show the directory entry itself
```

Why we use it:

```text
Permission denied on a file
-> file permissions may be correct
-> parent directory may block access
-> check directory permissions too
```

---

### Question 10: What does `umask 022` usually produce for new files and directories?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
umask 022
```

Meaning:

```text
New files usually start from 666
666 - 022 = 644

New directories usually start from 777
777 - 022 = 755
```

Result:

```text
files       = 644
directories = 755
```

Why we use it:

```text
Need default permissions for new files
-> umask removes permissions
-> prevents overly open files by default
```

---

## Users, Groups, And Sudo

### Question 11: What commands show your current user and group membership?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
whoami
id
groups
```

Meaning:

```text
whoami = current username
id = UID, GID, and groups
groups = group memberships
```

Why we use it:

```text
Permission denied
-> identify who you are
-> identify your groups
-> compare with file ownership and permissions
```

---

### Question 12: What command adds user `rahul` to group `developers`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo usermod -aG developers rahul
```

Meaning:

```text
usermod = modify user
-aG = append to supplementary group
developers = group name
rahul = username
```

Why we use it:

```text
User needs group-based access
-> add user to correct group
-> user may need to log out and back in
-> verify with id rahul
```

Important:

```text
Use -aG, not just -G, or you may replace existing supplementary groups.
```

---

### Question 13: What command checks what sudo commands your user can run?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo -l
```

Meaning:

```text
sudo = run commands with elevated privilege
-l = list allowed commands
```

Why we use it:

```text
Need admin action
-> check what you are allowed to run
-> avoid assuming full root access
```

---

### Question 14: What command safely edits sudoers rules?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo visudo
```

For a separate sudoers file:

```bash
sudo visudo -f /etc/sudoers.d/app-operators
```

Meaning:

```text
visudo = edit sudoers with syntax checking
```

Why we use it:

```text
Need to change sudo permissions
-> use visudo
-> syntax is checked before saving
-> avoid breaking sudo access
```

---

## Processes And Resource Monitoring

### Question 15: What command lists all processes in detailed format?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ps aux
```

Meaning:

```text
a = processes from all users
u = user-oriented detailed output
x = include processes without a terminal
```

Why we use it:

```text
Need to inspect running processes
-> list processes
-> check PID, user, CPU, memory, command
```

`ps aux` and `ps axu` usually work the same on Linux because flag order does not matter here. Use `ps aux`.

---

### Question 16: What command shows live CPU and memory usage by process?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
top
```

Alternative:

```bash
htop
```

Meaning:

```text
top = live process and resource view
htop = interactive nicer version, if installed
```

Why we use it:

```text
High CPU or memory alert
-> view live resource usage
-> identify suspicious process
-> investigate before killing anything
```

---

### Question 17: What is the safer first command to stop process `4821`, and when would you use `kill -9`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
kill 4821
```

Only if justified:

```bash
kill -9 4821
```

Meaning:

```text
kill PID = send SIGTERM, graceful stop request
kill -9 PID = send SIGKILL, immediate forced kill
```

Why we use it:

```text
Process is stuck
-> inspect it first
-> try graceful termination
-> verify
-> force kill only if it refuses to stop
```

---

### Question 18: What command finds zombie processes?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ps aux | awk '$8 ~ /Z/ {print}'
```

Meaning:

```text
ps aux = list processes
awk '$8 ~ /Z/' = find processes whose STAT column contains Z
Z = zombie
```

Why we use it:

```text
Suspect zombie process
-> find process with Z state
-> inspect parent process
-> usually fix parent, not zombie itself
```

---

## Services And Systemd

### Question 19: What command checks the status of `nginx`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
systemctl status nginx
```

Meaning:

```text
systemctl = control/query systemd services
status = show current service state
nginx = service name
```

Why we use it:

```text
Service issue
-> first check whether it is active, inactive, or failed
-> read PID and recent log lines
```

---

### Question 20: What is the difference between `systemctl start nginx` and `systemctl enable nginx`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo systemctl start nginx
sudo systemctl enable nginx
```

Meaning:

```text
start = start the service now
enable = start the service automatically at boot
```

Why we use it:

```text
Need service running immediately
-> start

Need service to survive reboot
-> enable
```

Shortcut:

```bash
sudo systemctl enable --now nginx
```

This starts it now and enables it at boot.

---

### Question 21: What is the difference between `systemctl restart nginx` and `systemctl reload nginx`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo systemctl restart nginx
sudo systemctl reload nginx
```

Meaning:

```text
restart = stop and start service again
reload = reload configuration without fully stopping, if supported
```

Why we use it:

```text
Config changed
-> prefer reload if supported
-> use restart when reload is unavailable or insufficient
```

Risk:

```text
restart can briefly interrupt traffic
reload is usually less disruptive
```

---

### Question 22: What commands check whether `nginx` is running now and enabled at boot?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
systemctl is-active nginx
systemctl is-enabled nginx
```

Meaning:

```text
is-active = running now?
is-enabled = configured to start at boot?
```

Why we use it:

```text
Need quick verification
-> active confirms current state
-> enabled confirms boot behavior
```

---

## Logs And Text Processing

### Question 23: What command shows the last 100 logs for `nginx`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
journalctl -u nginx -n 100 --no-pager
```

Meaning:

```text
journalctl = read systemd journal logs
-u nginx = only logs for nginx service
-n 100 = last 100 lines
--no-pager = print directly in terminal
```

Why we use it:

```text
Service has issue
-> need to know why
-> read recent service logs
```

Common mistake:

```text
head 100 shows first lines, not latest logs.
nginx by itself is not the log source.
```

---

### Question 24: What command follows `/var/log/app.log` live?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
tail -f /var/log/app.log
```

Meaning:

```text
tail = show end of file
-f = follow new lines live
```

Why we use it:

```text
Restart app or reproduce issue
-> watch fresh log lines
-> see current errors as they happen
```

---

### Question 25: What command searches for `ERROR` in `app.log`, ignoring uppercase/lowercase?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
grep -i "ERROR" app.log
```

Meaning:

```text
grep = search text
-i = ignore case
"ERROR" = search pattern
app.log = file
```

Why we use it:

```text
Large log file
-> reduce noise
-> find relevant error lines quickly
```

---

### Question 26: What command shows matching `ERROR` lines with line numbers?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
grep -n "ERROR" app.log
```

Meaning:

```text
-n = show line numbers
```

Why we use it:

```text
Need exact error location
-> search logs or config
-> line number helps inspect nearby context
```

---

### Question 27: What command prints the first field from every line of `file.txt`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
awk '{print $1}' file.txt
```

Meaning:

```text
awk = process text by fields
$1 = first field
print = output it
```

Why we use it:

```text
Command output has columns
-> extract only the field you need
-> simplify analysis
```

---

### Question 28: What command replaces `foo` with `bar` in the output of `file.txt`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sed 's/foo/bar/g' file.txt
```

Meaning:

```text
sed = stream editor
s/foo/bar/g = substitute foo with bar globally on each line
```

Why we use it:

```text
Need text transformation
-> preview transformed output
-> use carefully before editing files in place
```

This command prints changed output to the terminal. It does not modify the file unless you use options like `-i`.

---

### Question 29: What is the difference between `journalctl -u nginx` and `tail -f /var/log/nginx/access.log`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
journalctl -u nginx
tail -f /var/log/nginx/access.log
```

Meaning:

```text
journalctl -u nginx = service/systemd logs for nginx
tail -f access.log = live incoming HTTP request log
```

Why we use it:

```text
Need service lifecycle errors
-> use journalctl

Need to see if requests reach nginx
-> use access log
```

For nginx errors:

```bash
tail -f /var/log/nginx/error.log
```

---

### Question 30: Why should you check logrotate before manually deleting large logs?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ls /etc/logrotate.d/
sudo logrotate -d /etc/logrotate.conf
```

Meaning:

```text
logrotate = rotates, compresses, and removes old logs
-d = debug mode, do not actually rotate
```

Why we use it:

```text
Logs are huge
-> check whether rotation is configured
-> test rotation safely
-> avoid deleting important evidence blindly
```

---

## Disk And Storage

### Question 31: What command shows disk usage of mounted filesystems?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
df -h
```

Meaning:

```text
df = disk filesystem usage
-h = human-readable sizes
```

Why we use it:

```text
Disk alert
-> identify which filesystem is full
-> decide where to investigate next
```

---

### Question 32: What command shows the total size of `/var/log`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
du -sh /var/log
```

Meaning:

```text
du = disk usage of files/directories
-s = summary only
-h = human-readable sizes
```

Why we use it:

```text
Filesystem is full
-> find which directory is consuming space
-> narrow down investigation
```

Difference:

```text
df = filesystem usage
du = directory/file usage
```

---

### Question 33: `/` is 95% full. What command flow do you run before deleting anything?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
df -h
sudo du -xh --max-depth=1 /
sudo du -xh --max-depth=1 /var
sudo du -xh --max-depth=1 /var/log
sudo find /var/log -type f -size +1G -ls
```

Meaning:

```text
df = which filesystem is full
du = which directory is large
find = which exact files are huge
```

Why we use it:

```text
Disk full incident
-> confirm full filesystem
-> locate large directory
-> locate exact large files
-> choose safe fix
```

---

### Question 34: What command lists block devices and disks?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
lsblk
```

Meaning:

```text
lsblk = list block devices
```

Why we use it:

```text
Storage issue or new disk
-> see disks, partitions, mountpoints
-> understand device layout
```

---

### Question 35: What commands show mounted filesystems?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
mount
findmnt
```

Meaning:

```text
mount = show mounted filesystems and mount options
findmnt = tree view of mounted filesystems
```

Why we use it:

```text
Path/storage problem
-> check whether filesystem is mounted
-> check where device is mounted
```

---

## Networking

### Question 36: What command shows IP addresses on the server?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ip addr
```

Short form:

```bash
ip a
```

Meaning:

```text
ip = network configuration tool
addr = show interface addresses
```

Why we use it:

```text
Network issue
-> confirm server has an IP
-> check interface state
```

---

### Question 37: What command shows the routing table and default gateway?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ip route
```

Meaning:

```text
ip route = show network routes
default = default gateway route
```

Why we use it:

```text
Cannot reach outside network
-> check route
-> confirm default gateway exists
```

---

### Question 38: What command shows listening TCP/UDP ports?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ss -tuln
```

With process names:

```bash
sudo ss -tulnp
```

Meaning:

```text
ss = socket statistics
-t = TCP
-u = UDP
-l = listening
-n = numeric ports
-p = process info
```

Why we use it:

```text
Service says active but app unreachable
-> check whether port is listening
-> identify process bound to port
```

Older alternative:

```bash
netstat -tuln
```

---

### Question 39: What is the difference between `ping`, `dig`, and `curl`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ping example.com
dig example.com
curl -v https://example.com
```

Meaning:

```text
ping = basic host reachability using ICMP
dig = DNS lookup
curl = test application protocol like HTTP/HTTPS
```

Why we use it:

```text
Website not reachable
-> ping checks basic reachability
-> dig checks DNS resolution
-> curl checks actual web response
```

Important:

```text
Ping success does not prove the website works.
Curl is closer to the user's real request.
```

---

### Question 40: Website is not responding, but service is active. What command flow do you run?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
systemctl status nginx
journalctl -u nginx -n 100 --no-pager
sudo ss -tulnp
curl -v http://localhost
ip addr
ip route
```

Meaning:

```text
systemctl = service state
journalctl = service logs
ss = listening ports
curl = local HTTP test
ip addr = IP address
ip route = routing
```

Why we use it:

```text
Service active does not mean app works
-> check logs
-> check port
-> test local response
-> check network basics
```

---

## SSH And Remote Access

### Question 41: What command connects to a remote server as `user`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ssh user@server
```

Meaning:

```text
ssh = secure shell remote login
user = remote username
server = hostname or IP
```

Why we use it:

```text
Need remote server access
-> open secure shell session
-> investigate or manage server
```

---

### Question 42: What command generates an Ed25519 SSH key?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
ssh-keygen -t ed25519
```

Meaning:

```text
ssh-keygen = create SSH key pair
-t ed25519 = key type
```

Why we use it:

```text
Need key-based login
-> generate private/public key pair
-> keep private key secret
-> copy public key to server
```

---

### Question 43: What command copies `app.conf` to `/tmp/` on a remote server?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
scp app.conf user@server:/tmp/
```

Meaning:

```text
scp = secure copy
app.conf = local file
user@server:/tmp/ = remote destination
```

Why we use it:

```text
Need to transfer file to server
-> copy securely over SSH
-> verify file on remote side
```

---

## Cron And Scheduled Tasks

### Question 44: What commands edit and list your cron jobs?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
crontab -e
crontab -l
```

Meaning:

```text
crontab -e = edit cron jobs
crontab -l = list cron jobs
```

Why we use it:

```text
Need scheduled task
-> edit crontab
-> list to verify it exists
```

---

### Question 45: What does this cron schedule mean: `*/5 * * * * /opt/scripts/check.sh`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```cron
*/5 * * * * /opt/scripts/check.sh
```

Meaning:

```text
*/5 = every 5 minutes
first * = every hour
second * = every day of month
third * = every month
fourth * = every day of week
```

Why we use it:

```text
Need repeated automation
-> schedule script
-> run every 5 minutes
```

Production reminder:

```text
Cron has a minimal environment.
Use absolute paths and redirect logs.
```

Example with logging:

```cron
*/5 * * * * /opt/scripts/check.sh >> /var/log/check.log 2>&1
```

---

## Package Management

### Question 46: What is the Ubuntu/Debian workflow to install `nginx` and start it?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
sudo apt update
sudo apt install nginx
systemctl status nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

Meaning:

```text
apt update = refresh package metadata
apt install = install package
status = check service state
start = run now
enable = start at boot
```

Why we use it:

```text
Need install software
-> update package index
-> install package
-> check whether service exists/runs
-> start and enable if required
```

---

### Question 47: What package managers are commonly used on Ubuntu/Debian and RHEL-family systems?

Your answer:

```text
Write your answer here.
```

Correct answer:

```text
Ubuntu/Debian: apt
RHEL-family modern systems: dnf
Older RHEL-family systems: yum
```

Examples:

```bash
sudo apt install nginx
sudo dnf install nginx
sudo yum install nginx
```

Why we use it:

```text
Need install/remove/update software
-> identify OS family
-> use correct package manager
```

Check OS:

```bash
cat /etc/os-release
```

---

## Shell Scripting

### Question 48: In shell scripting, what is wrong with `APP = "payment-service"`?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
APP="payment-service"
```

Meaning:

```text
No spaces around = in shell variable assignment.
```

Why we use it:

```text
Need reusable values in scripts
-> store service name in variable
-> reference it later as "$APP"
```

Example:

```bash
APP="payment-service"
systemctl status "$APP"
```

---

### Question 49: What do `$1`, `$2`, `$#`, `$@`, and `$?` mean in a shell script?

Your answer:

```text
Write your answer here.
```

Correct answer:

```text
$1 = first argument
$2 = second argument
$# = number of arguments
$@ = all arguments
$? = exit status of previous command
```

Why we use it:

```text
Need reusable script
-> accept input arguments
-> check whether previous command succeeded
-> make script behave conditionally
```

Example:

```bash
./check-service.sh nginx production
```

Then:

```text
$1 = nginx
$2 = production
```

---

## Production Troubleshooting

### Question 50: A payment service is intermittently unavailable. What complete investigation flow do you run before fixing?

Your answer:

```text
Write your answer here.
```

Correct answer:

```bash
hostname
uptime
top
free -h
df -h
sudo du -xh --max-depth=1 /var
sudo find /var/log -type f -size +1G -ls
systemctl status payment-service
journalctl -u payment-service -p err -n 100 --no-pager
sudo ss -tulnp
curl -v http://localhost:8080/health
```

Meaning:

```text
hostname = confirm server
uptime/top = CPU/load/process pressure
free -h = memory
df/du/find = disk usage
systemctl = service state
journalctl = service errors
ss = listening ports
curl = app response
```

Why we use it:

```text
Incident reported
-> identify server
-> check resource pressure
-> check disk
-> check service state
-> check logs
-> check port
-> test health endpoint
-> fix only after evidence
-> verify after fix
```

Avoid blindly:

```bash
rm -rf /var/log/*
kill -9 <PID>
chmod 777 <file>
reboot
```

Final principle:

```text
Problem
-> evidence
-> diagnosis
-> smallest safe fix
-> verification
```

