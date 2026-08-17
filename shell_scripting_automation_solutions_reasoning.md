# Shell Scripting & Automation — Solutions, Reasoning & Production Thinking

This is the **answer key** for the practice workbook.

For every question, the goal is not only to know the command. You should understand:

```text
Problem
  ↓
Evidence needed
  ↓
Command
  ↓
Meaning of output
  ↓
Safest action
  ↓
Verification
```

---

# Part 1 — Direct Questions

## Q1 — What is a shell script?

### Answer

A shell script is a text file containing shell commands that Linux can execute.

Example:

```bash
#!/bin/bash
echo "Checking server"
hostname
uptime
df -h
```

### Why?

Instead of manually running several commands every time, a script makes the process repeatable and automatable.

### Production use

Shell scripts are commonly used for:

- health checks
- backups
- deployments
- log checks
- cleanup
- service checks
- scheduled maintenance

---

## Q2 — Shebang

### Answer

```bash
#!/bin/bash
```

This tells Linux to use Bash to interpret the script.

Think:

```text
script
  ↓
#!/bin/bash
  ↓
Bash
  ↓
commands execute
```

---

## Q3 — Create and run a script

```bash
nano server-check.sh
```

Add your commands, save, then:

```bash
chmod +x server-check.sh
./server-check.sh
```

### Why?

`chmod +x` adds execute permission.

---

## Q4 — echo

```bash
echo "Server is healthy"
```

prints:

```text
Server is healthy
```

### Why?

Scripts need a way to display information, warnings, and errors.

---

## Q5 — Variables

```bash
APP_NAME="employee-portal"
echo "$APP_NAME"
```

### Why?

Variables store values so they can be reused.

For example:

```bash
APP_NAME="employee-portal"

systemctl status "$APP_NAME"
journalctl -u "$APP_NAME"
```

---

## Q6 — Variable syntax

Wrong:

```bash
APP_NAME = "employee-portal"
```

Correct:

```bash
APP_NAME="employee-portal"
```

### Why?

Bash variable assignment does not allow spaces around `=`.

---

## Q7 — Command substitution

```bash
SERVER_NAME=$(hostname)
```

### Why?

`$(...)` executes a command and puts its output into the variable.

Example:

```bash
DATE=$(date)
```

---

## Q8 — Exit status

```bash
echo $?
```

shows the exit status of the previous command.

Normally:

```text
0      → success
nonzero → failure/error
```

### Why?

Automation needs to know whether an operation succeeded.

---

## Q9 — File condition

```bash
if [ -f "/tmp/test.txt" ]
then
    echo "File exists"
fi
```

`-f` means the path exists and is a regular file.

---

## Q10 — Directory condition

```bash
if [ -d "/var/log" ]
then
    echo "Directory exists"
fi
```

`-d` checks for a directory.

---

## Q11 — Number comparison

For "greater than":

```bash
-gt
```

Example:

```bash
if [ "$CPU" -gt 80 ]
then
    echo "CPU is high"
fi
```

Useful operators:

```text
-eq  equal
-ne  not equal
-gt  greater than
-ge  greater/equal
-lt  less than
-le  less/equal
```

---

## Q12 — String comparison

```bash
if [ "$ENVIRONMENT" = "production" ]
then
    echo "Production"
fi
```

Use `=` for text comparison.

---

## Q13 — for loop

```bash
for file in *.log
do
    echo "$file"
done
```

### Why?

It performs the same operation for every matching file.

---

## Q14 — for vs while

### `for`

Use when iterating over a collection:

```bash
for file in *.log
do
    grep "ERROR" "$file"
done
```

### `while`

Use when continuing while a condition is true:

```bash
while ! systemctl is-active --quiet payment-service
do
    sleep 5
done
```

Mental model:

```text
for   → for every item
while → while condition remains true
```

---

## Q15 — sleep

```bash
sleep 10
```

waits 10 seconds.

### Production use

Useful when waiting for:

- a service to start
- a deployment to finish
- a retry interval
- another process

---

## Q16 — Script arguments

For:

```bash
./deploy.sh employee-portal production
```

```text
$0 = ./deploy.sh
$1 = employee-portal
$2 = production
$# = 2
$@ = employee-portal production
```

### Why?

Arguments make scripts reusable.

---

## Q17 — Functions

Example:

```bash
check_disk() {
    df -h /
}

check_disk
```

### Why?

Functions let you create reusable blocks of logic.

---

## Q18 — `&&` and `||`

```bash
command1 && command2
```

runs `command2` only if `command1` succeeds.

```bash
command1 || command2
```

runs `command2` if `command1` fails.

Example:

```bash
systemctl restart nginx && echo "Success"
```

---

## Q19 — Pipe

```bash
ps aux | grep nginx
```

The output of `ps aux` becomes input to `grep nginx`.

Think:

```text
ps aux
 ↓
all processes
 ↓
grep nginx
 ↓
matching processes
```

---

## Q20 — `>` vs `>>`

```bash
command > output.log
```

overwrites the file.

```bash
command >> output.log
```

appends to the file.

### Production warning

Using `>` on an important log can overwrite existing content.

---

## Q21 — `2>`

```bash
command 2> error.log
```

redirects standard error to `error.log`.

Linux standard streams:

```text
0 = stdin
1 = stdout
2 = stderr
```

---

## Q22 — `2>&1`

```bash
command > output.log 2>&1
```

sends both normal output and errors into `output.log`.

This is especially useful with cron jobs.

---

# Part 2 — Basic Automation Scenarios

# Scenario 1 — Server health check

### Good solution

```bash
#!/bin/bash

echo "===== SERVER HEALTH ====="

echo "Hostname:"
hostname

echo "Uptime:"
uptime

echo "Memory:"
free -h

echo "Disk:"
df -h /

echo "Top CPU processes:"
ps aux --sort=-%cpu | head -5

echo "========================="
```

### Why these commands?

```text
hostname
→ identify server

uptime
→ uptime/load information

free -h
→ memory

df -h
→ filesystem usage

ps aux --sort=-%cpu
→ CPU-heavy processes

head
→ keep output manageable
```

---

# Scenario 2 — Disk warning

### Solution

```bash
#!/bin/bash

USAGE=$(df -h / | awk 'NR==2 {print $5}' | tr -d '%')

echo "Disk usage: $USAGE%"

if [ "$USAGE" -ge 90 ]
then
    echo "CRITICAL: Disk usage is $USAGE%"

elif [ "$USAGE" -ge 80 ]
then
    echo "WARNING: Disk usage is $USAGE%"

else
    echo "OK: Disk usage is $USAGE%"
fi
```

### Reasoning

First:

```bash
df -h /
```

answers:

> How full is the filesystem?

Then we extract the percentage.

Then:

```bash
if
```

makes a decision.

`tr -d '%'` changes:

```text
82%
```

into:

```text
82
```

so Bash can compare it numerically.

---

# Scenario 3 — Application status

```bash
SERVICE="payment-service"

if systemctl is-active --quiet "$SERVICE"
then
    echo "$SERVICE is running"
else
    echo "ALERT: $SERVICE is DOWN"
fi
```

### Why?

The requirement is specifically asking whether the service is active.

`systemctl is-active` is designed for that check.

`--quiet` suppresses unnecessary output because the `if` only needs the exit status.

---

# Scenario 4 — Multiple applications

```bash
SERVICES="nginx redis payment-service employee-portal"

for SERVICE in $SERVICES
do
    if systemctl is-active --quiet "$SERVICE"
    then
        echo "$SERVICE: RUNNING"
    else
        echo "$SERVICE: DOWN"
    fi
done
```

### Why?

The same check must be repeated for multiple services.

That naturally suggests:

```text
list
 ↓
for loop
 ↓
same check
```

---

# Scenario 5 — Search logs

```bash
for file in *.log
do
    echo "Checking $file"
    grep -i "ERROR" "$file"
done
```

### Reasoning

The task contains:

```text
every log
+
search ERROR
```

Therefore:

```text
for + grep
```

---

# Scenario 6 — Count errors

```bash
grep -ic "ERROR" app.log
```

### Why?

`grep` searches.

`-i` ignores case.

`-c` counts matching lines.

---

# Scenario 7 — Watch logs

```bash
tail -f app.log
```

### Why?

`tail` shows the end of a file.

`-f` follows new lines as they are written.

Useful during:

- deployment
- application startup
- debugging
- incidents

---

# Scenario 8 — Restart and verify

```bash
sudo systemctl restart nginx
sudo systemctl status nginx
```

Better automation:

```bash
sudo systemctl restart nginx

if systemctl is-active --quiet nginx
then
    echo "nginx is running"
else
    echo "nginx failed to start"
fi
```

### Key lesson

A command changing something is not enough.

Always think:

```text
CHANGE
 ↓
VERIFY
```

---

# Scenario 9 — Restart only if down

```bash
SERVICE="employee-portal"

if systemctl is-active --quiet "$SERVICE"
then
    echo "$SERVICE is already running"
else
    echo "$SERVICE is down. Starting..."
    sudo systemctl start "$SERVICE"
fi
```

### Why `start` instead of `restart`?

The requirement says:

> If it is already running, do nothing.

So restarting a healthy service would be unnecessary.

---

# Scenario 10 — Disk investigation

Do not immediately delete files.

Start:

```bash
df -h
```

Then narrow the location:

```bash
sudo du -xh --max-depth=1 /
```

If `/var` is large:

```bash
sudo du -xh --max-depth=1 /var
```

If `/var/log` is large:

```bash
sudo du -xh --max-depth=1 /var/log
```

Then find large files:

```bash
sudo find /var/log -type f -size +1G -ls
```

### Correct thinking

```text
filesystem
 ↓
large directory
 ↓
large subdirectory
 ↓
large files
 ↓
identify cause
 ↓
safe cleanup
 ↓
verify
```

---

# Scenario 11 — Large log files

```bash
sudo find /var/log -type f -size +1G -ls
```

### Why?

If logs are suspected, search the log directory first.

Searching `/` can be:

- slower
- noisier
- more likely to encounter permission restrictions
- unnecessary

If searching `/` is required:

```bash
sudo find / -type f -size +1G -ls 2>/dev/null
```

---

# Scenario 12 — Configuration check

```bash
if [ -f "/etc/employee-portal/app.conf" ]
then
    echo "Configuration found"
else
    echo "Configuration missing"
fi
```

### Why?

Validate required files before trying to use or modify them.

---

# Scenario 13 — Backup before change

```bash
CONFIG="/etc/nginx/nginx.conf"
BACKUP="/etc/nginx/nginx.conf.backup"

if [ -f "$CONFIG" ]
then
    sudo cp "$CONFIG" "$BACKUP"
    echo "Backup created: $BACKUP"
else
    echo "ERROR: Configuration file does not exist"
    exit 1
fi
```

### Production principle

```text
Check
 ↓
Backup
 ↓
Change
 ↓
Verify
```

---

# Scenario 14 — Timestamped backup

```bash
CONFIG="/etc/nginx/nginx.conf"
TIMESTAMP=$(date +"%Y-%m-%d-%H%M")
BACKUP="${CONFIG}.backup-${TIMESTAMP}"

sudo cp "$CONFIG" "$BACKUP"

echo "Backup created: $BACKUP"
```

### Why?

A timestamp gives you multiple identifiable restore points.

---

# Scenario 15 — Multiple directories

```bash
DIRECTORIES="/var/log /opt /home /data"

for DIR in $DIRECTORIES
do
    echo "Checking $DIR"
    du -sh "$DIR"
done
```

### Why?

Same operation + multiple paths = loop.

---

# Scenario 16 — Retry application startup

```bash
SERVICE="payment-service"

while ! systemctl is-active --quiet "$SERVICE"
do
    echo "Waiting for $SERVICE..."
    sleep 5
done

echo "$SERVICE is active"
```

### Important limitation

This can run forever if the service never starts.

A production script should normally have a maximum retry count.

---

# Scenario 17 — Maximum retry

```bash
SERVICE="payment-service"
MAX_ATTEMPTS=6
ATTEMPT=1

while [ "$ATTEMPT" -le "$MAX_ATTEMPTS" ]
do
    if systemctl is-active --quiet "$SERVICE"
    then
        echo "$SERVICE is active"
        exit 0
    fi

    echo "Attempt $ATTEMPT/$MAX_ATTEMPTS"
    sleep 5
    ATTEMPT=$((ATTEMPT + 1))
done

echo "ERROR: $SERVICE did not become active"
exit 1
```

### Why?

Bounded retries prevent an automation job from running forever.

---

# Part 3 — Arguments

# Scenario 18 — Generic service checker

```bash
SERVICE="$1"

if [ -z "$SERVICE" ]
then
    echo "Usage: $0 <service-name>"
    exit 1
fi

if systemctl is-active --quiet "$SERVICE"
then
    echo "$SERVICE is running"
else
    echo "$SERVICE is not running"
fi
```

Run:

```bash
./check-service.sh nginx
```

### Why?

`$1` makes the script reusable for different services.

---

# Scenario 19 — Missing argument

```bash
if [ -z "$1" ]
then
    echo "Usage: $0 <service-name>"
    exit 1
fi
```

### Why?

Validate input before doing work.

---

# Scenario 20 — Environment argument

```bash
APP="$1"
ENVIRONMENT="$2"

if [ -z "$APP" ] || [ -z "$ENVIRONMENT" ]
then
    echo "Usage: $0 <application> <environment>"
    exit 1
fi

echo "Application: $APP"
echo "Environment: $ENVIRONMENT"
```

---

# Part 4 — Production Scenarios

# Scenario 21 — Production confirmation

```bash
SERVICE="$1"
ENVIRONMENT="$2"

if [ -z "$SERVICE" ] || [ -z "$ENVIRONMENT" ]
then
    echo "Usage: $0 <service> <environment>"
    exit 1
fi

if [ "$ENVIRONMENT" = "production" ]
then
    read -p "Production restart. Continue? (yes/no): " CONFIRM

    if [ "$CONFIRM" != "yes" ]
    then
        echo "Operation cancelled"
        exit 0
    fi
fi

sudo systemctl restart "$SERVICE"
```

### Why?

Production operations should have stronger guardrails.

---

# Scenario 22 — Health-check all services

```bash
SERVICES="nginx redis payment-service employee-portal"

for SERVICE in $SERVICES
do
    if systemctl is-active --quiet "$SERVICE"
    then
        echo "Service: $SERVICE"
        echo "Status: RUNNING"
    else
        echo "Service: $SERVICE"
        echo "Status: DOWN"
    fi

    echo "--------------------"
done
```

### Pattern

```text
list
 ↓
loop
 ↓
check
 ↓
decision
 ↓
report
```

---

# Scenario 23 — Health report

```bash
LOG="server-health.log"

{
    echo "===== SERVER HEALTH ====="
    echo "Date: $(date)"
    echo "Hostname: $(hostname)"
    echo "Uptime: $(uptime)"
    echo "Memory:"
    free -h
    echo "Disk:"
    df -h /
    echo "========================="
} >> "$LOG"
```

### Why?

The block groups multiple commands so the complete report can be redirected to one file.

---

# Scenario 24 — Exit codes

Example:

```bash
if systemctl is-active --quiet payment-service
then
    echo "Service healthy"
else
    echo "Service down"
    exit 1
fi

exit 0
```

Then:

```bash
./health-check.sh
echo $?
```

### Why?

Monitoring systems, cron, CI/CD, and other scripts can use the exit code to determine success/failure.

---

# Scenario 25 — Log monitoring

Basic learning solution:

```bash
while true
do
    if grep -q "CRITICAL" /var/log/payment.log
    then
        echo "ALERT: CRITICAL entry found"
    fi

    sleep 30
done
```

### What this teaches

```text
while
+
grep
+
condition
+
sleep
```

### Production improvement

This repeatedly scans the entire file. For a large log, a better production design would process only new entries or use the logging/monitoring system already deployed.

---

# Part 5 — Cron

# Scenario 26 — Daily backup

```cron
0 2 * * * /opt/scripts/backup.sh
```

Cron fields:

```text
minute hour day-of-month month day-of-week
  0     2       *          *       *
```

Meaning:

> Every day at 02:00.

---

# Scenario 27 — Every 5 minutes

```cron
*/5 * * * * /opt/scripts/check-payment.sh
```

`*/5` means every five minutes.

---

# Scenario 28 — Cron logging

```cron
*/5 * * * * /opt/scripts/check-payment.sh >> /var/log/payment-check.log 2>&1
```

Meaning:

```text
>>   append normal output
2>&1 send errors to same log
```

---

# Scenario 29 — Cron job didn't run

Use this troubleshooting sequence.

### 1. Check the crontab

```bash
crontab -l
```

### 2. Run the script manually

```bash
/opt/scripts/check.sh
```

### 3. Check permissions

```bash
ls -l /opt/scripts/check.sh
```

### 4. Check cron daemon

Depending on Linux distribution:

```bash
systemctl status cron
```

or:

```bash
systemctl status crond
```

### 5. Check cron logs

```bash
journalctl -u cron
```

or:

```bash
journalctl -u crond
```

### 6. Check absolute paths

Cron's environment may differ from your terminal.

### Why this order?

You move from configuration → script → permissions → scheduler → logs.

---

# Scenario 30 — Works manually but not in cron

Possible causes:

1. Different `PATH`
2. Relative paths
3. Missing environment variables
4. Different working directory
5. Permissions
6. Different user
7. Shell/interpreter assumptions
8. Missing output/error logging

### Important lesson

Cron does not necessarily have the same environment as your interactive shell.

---

# Scenario 31 — Permission problem

Start with:

```bash
id
```

Then:

```bash
ls -l /opt/scripts/backup.sh
```

Then:

```bash
ls -ld /opt/scripts
```

Check groups:

```bash
groups
```

Check sudo permissions if relevant:

```bash
sudo -l
```

### Correct thinking

```text
Who am I?
 ↓
Who owns it?
 ↓
What permissions exist?
 ↓
Can I access parent directories?
 ↓
Do I need sudo?
 ↓
What is the minimum safe fix?
```

Do not automatically use:

```bash
chmod 777
```

---

# Scenario 32 — Script silently fails

A good script should:

```bash
echo "Starting backup..."
```

and check important operations:

```bash
if backup_command
then
    echo "Backup successful"
else
    echo "Backup failed"
    exit 1
fi
```

Run with logging:

```bash
./backup.sh >> /var/log/backup.log 2>&1
```

### Principle

Automation needs observability:

```text
Action
 ↓
Result
 ↓
Log
 ↓
Exit status
```

---

# Scenario 33 — Dangerous `rm -rf`

Given:

```bash
rm -rf "$DIRECTORY"
```

### Risk

If `$DIRECTORY` is wrong, empty, or unexpectedly points somewhere important, destructive deletion may occur.

At minimum validate the variable:

```bash
if [ -z "$DIRECTORY" ]
then
    echo "Directory is empty"
    exit 1
fi
```

Also explicitly reject dangerous paths where appropriate.

### Production lesson

Never solve a disk-full incident by blindly deleting data.

First determine what is consuming the disk and why.

---

# Scenario 34 — Infinite loop

Given:

```bash
while true
do
    systemctl status payment-service
done
```

### Problems

- no delay
- no exit condition
- repeated command execution
- unnecessary resource consumption

Basic improvement:

```bash
while true
do
    systemctl status payment-service
    sleep 10
done
```

Better still, add a meaningful stopping condition or maximum retries.

---

# Part 6 — DevOps Troubleshooting

# Scenario 35 — Payment service is down

A good investigation:

```text
1. Confirm service status.
2. Check process.
3. Check recent logs.
4. Check disk.
5. Check memory/CPU.
6. Check configuration.
7. Check dependencies.
8. Recover if appropriate.
9. Verify.
```

Useful commands:

```bash
systemctl status payment-service
ps aux | grep payment
journalctl -u payment-service -n 100
df -h
free -h
```

### Why?

A service being down is a symptom. The root cause could be configuration, dependency, disk, memory, application failure, etc.

---

# Scenario 36 — Automated incident checker

Example:

```bash
#!/bin/bash

echo "===== INCIDENT CHECK ====="

echo "Hostname:"
hostname

echo "Uptime:"
uptime

echo "Disk:"
df -h /

echo "Memory:"
free -h

echo "Payment service:"
systemctl status payment-service --no-pager

echo "Recent errors:"
journalctl -u payment-service -p err -n 20 --no-pager

echo "=========================="
```

### Why?

It gathers common evidence into one repeatable report.

---

# Scenario 37 — Multiple servers

Basic approach:

```bash
SERVERS="app01 app02 app03 app04"

for SERVER in $SERVERS
do
    echo "Checking $SERVER"

    if ssh -o ConnectTimeout=5 "$SERVER" \
        "systemctl is-active --quiet payment-service"
    then
        echo "$SERVER: RUNNING"
    else
        echo "$SERVER: DOWN or unreachable"
    fi
done
```

### Important distinction

A failed SSH connection does not prove the application is down.

It could mean:

```text
network problem
SSH problem
server unavailable
authentication problem
application down
```

Good troubleshooting distinguishes these possibilities.

---

# Scenario 38 — Deployment verification

A good sequence:

```text
1. Check service
2. Check application endpoint
3. Check disk
4. Check recent logs
5. Confirm expected behavior
```

Possible commands:

```bash
systemctl is-active payment-service
```

If there is a health endpoint:

```bash
curl http://localhost:8080/health
```

Disk:

```bash
df -h
```

Logs:

```bash
journalctl -u payment-service -n 100 --no-pager
```

Errors:

```bash
journalctl -u payment-service -n 100 --no-pager | grep -i "ERROR"
```

### Why?

A running process does not necessarily mean the application is healthy.

---

# Scenario 39 — Production deployment helper

The important thing here is to **break the large problem into small requirements**.

```text
Arguments
 ↓
Validate input
 ↓
Production confirmation
 ↓
Disk safety check
 ↓
Service validation
 ↓
Restart
 ↓
Retry
 ↓
Verify
 ↓
Inspect logs
 ↓
Exit status
```

One reasonable implementation:

```bash
#!/bin/bash

SERVICE="$1"
ENVIRONMENT="$2"

if [ -z "$SERVICE" ] || [ -z "$ENVIRONMENT" ]
then
    echo "Usage: $0 <service> <environment>"
    exit 1
fi

echo "Service: $SERVICE"
echo "Environment: $ENVIRONMENT"

if [ "$ENVIRONMENT" = "production" ]
then
    read -p "Production restart. Continue? (yes/no): " CONFIRM

    if [ "$CONFIRM" != "yes" ]
    then
        echo "Cancelled"
        exit 0
    fi
fi

USAGE=$(df -h / | awk 'NR==2 {print $5}' | tr -d '%')

echo "Disk usage: $USAGE%"

if [ "$USAGE" -ge 90 ]
then
    echo "CRITICAL: Disk usage too high"
    exit 1
fi

if ! systemctl cat "$SERVICE" >/dev/null 2>&1
then
    echo "ERROR: Service not found"
    exit 1
fi

if ! sudo systemctl restart "$SERVICE"
then
    echo "ERROR: Restart failed"
    exit 1
fi

MAX_ATTEMPTS=6
ATTEMPT=1

while [ "$ATTEMPT" -le "$MAX_ATTEMPTS" ]
do
    if systemctl is-active --quiet "$SERVICE"
    then
        echo "SUCCESS: $SERVICE is active"
        break
    fi

    echo "Waiting... attempt $ATTEMPT/$MAX_ATTEMPTS"
    sleep 5
    ATTEMPT=$((ATTEMPT + 1))
done

if ! systemctl is-active --quiet "$SERVICE"
then
    echo "FAILURE: $SERVICE did not become active"
    journalctl -u "$SERVICE" -n 50 --no-pager
    exit 1
fi

echo "Recent logs:"
journalctl -u "$SERVICE" -n 20 --no-pager

echo "Deployment check completed successfully"
exit 0
```

### Important note

This is a **learning solution**, not a universal production deployment script. Real production environments should also consider authentication, application health endpoints, rollback, timeouts, dependency checks, locking/concurrency, structured logging, and environment-specific controls.

---

# Part 7 — Thinking Test

# Q40 — Disk is 95% full

### Strong answer

```text
1. Confirm which filesystem is full.
2. Identify large directories.
3. Narrow down the largest directory.
4. Find large files.
5. Determine what those files are.
6. Decide whether they can safely be removed/rotated.
7. Apply the fix.
8. Verify disk usage again.
```

Commands:

```bash
df -h
du -xh --max-depth=1 /
du -xh --max-depth=1 /var
find /var/log -type f -size +1G -ls
```

### Key improvement

Do not immediately use:

```bash
rm -rf
```

Investigate first.

---

# Q41 — Application is not responding

### Strong answer

```text
1. Is the service running?
2. Is the process running?
3. Is the expected port listening?
4. Are there errors in logs?
5. Are CPU/memory/disk healthy?
6. Are dependencies available?
7. Does localhost respond?
8. Is the network path working?
9. Fix the actual cause.
10. Verify again.
```

Useful commands:

```bash
systemctl status payment-service
ps aux | grep payment
ss -lntp
journalctl -u payment-service -n 100
free -h
df -h
curl http://localhost:8080/health
```

---

# Q42 — Script works manually but not through cron

### Strong answer

```text
1. Check crontab.
2. Check cron/crond status.
3. Run script manually.
4. Check permissions.
5. Check absolute paths.
6. Check environment variables.
7. Check working-directory assumptions.
8. Capture stdout/stderr.
9. Check cron logs.
10. Verify the next execution.
```

### Core idea

Cron's execution environment can differ from your interactive shell.

---

# Q43 — Permission denied

### Strong answer

```text
1. Identify current user.
2. Check file owner.
3. Check file permissions.
4. Check parent directory permissions.
5. Check group membership.
6. Check whether sudo is required.
7. Apply the smallest safe permission change.
8. Test again.
```

Commands:

```bash
id
ls -l file
ls -ld directory
groups
sudo -l
```

### Important

Do not make everything world-writable with:

```bash
chmod 777
```

That can create security problems.

---

# Q44 — Process using 100% CPU

### Strong answer

```text
1. Identify the process.
2. Confirm CPU usage.
3. Identify owner.
4. Determine whether the process is expected.
5. Check logs.
6. Check application behavior.
7. Decide whether usage is temporary or abnormal.
8. Try graceful recovery if needed.
9. Force-kill only when justified.
10. Verify system health.
```

Useful commands:

```bash
top
ps aux --sort=-%cpu | head
ps -fp <PID>
```

### Key lesson

High CPU is a symptom, not automatically a reason to kill the process.

---

# Q45 — Service keeps stopping

### Strong answer

```text
1. Check service status.
2. Check recent logs.
3. Check configuration.
4. Check dependencies.
5. Check ports.
6. Check disk and memory.
7. Look for conflicting processes.
8. Identify root cause.
9. Fix cause.
10. Restart and verify.
```

Useful commands:

```bash
systemctl status payment-service
journalctl -u payment-service -n 100
df -h
free -h
ss -lntp
```

---

# Final Thinking Challenge

## A — "The server is slow."

### Strong thought process

```text
1. Check CPU.
2. Check memory.
3. Check load average.
4. Check disk/filesystem.
5. Identify resource-heavy processes.
6. Check application/service behavior.
7. Check logs.
8. Compare against normal behavior.
9. Identify root cause.
10. Fix and verify.
```

Commands may include:

```bash
top
free -h
uptime
df -h
ps aux --sort=-%cpu | head
journalctl -p err -n 50
```

---

## B — "The application is down."

### Strong thought process

```text
1. Confirm service status.
2. Confirm process status.
3. Check logs.
4. Check listening port.
5. Check dependencies.
6. Check resources.
7. Attempt safe recovery.
8. Verify application response.
```

---

## C — "The disk is full."

### Strong thought process

```text
1. Confirm filesystem.
2. Find large directories.
3. Narrow down location.
4. Find large files.
5. Identify why they exist.
6. Decide safe cleanup/rotation.
7. Make change.
8. Verify free space.
```

---

## D — "The cron job didn't execute."

### Strong thought process

```text
1. Check crontab.
2. Check cron/crond.
3. Run script manually.
4. Check permissions.
5. Check absolute paths.
6. Check environment.
7. Capture output/errors.
8. Check cron logs.
9. Fix.
10. Verify next execution.
```

---

# The Most Important Production Mindset

When someone says:

> "The server is slow."

Do not randomly run commands.

Think:

```text
SYMPTOM
   ↓
EVIDENCE
   ↓
HYPOTHESIS
   ↓
TEST
   ↓
ROOT CAUSE
   ↓
SAFE FIX
   ↓
VERIFICATION
```

Example:

```text
Application is slow
       ↓
Check CPU
       ↓
CPU is high
       ↓
Find process
       ↓
payment-service
       ↓
Check logs
       ↓
Repeated application error
       ↓
Find root cause
       ↓
Fix
       ↓
Verify CPU + application
```

This thinking is more valuable than memorizing commands.

---

# Command Relationships

Remember the concepts as a connected system:

```text
echo
  ↓
display information

variables
  ↓
store information

$(command)
  ↓
capture command output

if
  ↓
make decisions

for
  ↓
repeat over items

while
  ↓
repeat while condition is true

$?
  ↓
know whether a command succeeded

functions
  ↓
reuse logic

|
  ↓
connect commands

>
>>
  ↓
save command output

cron
  ↓
run automation on a schedule
```

And:

```text
Linux commands
      ↓
Shell script
      ↓
Variables + conditions + loops
      ↓
Automation
      ↓
Cron / CI/CD / deployment
      ↓
Production operations
```

# Self-Review Checklist

When answering a scenario, ask yourself:

## 1. Did I investigate before changing anything?

Good:

```text
Check → understand → fix → verify
```

Bad:

```text
Restart → kill → delete → hope
```

## 2. Did I choose the least destructive action?

Do not jump directly to:

```bash
kill -9
rm -rf
chmod 777
reboot
```

without understanding why.

## 3. Did I verify the result?

After changing something:

```text
Did it actually work?
```

Always perform an appropriate verification.

## 4. Did I make the script reusable?

Instead of hardcoding:

```bash
systemctl restart nginx
```

you can sometimes use:

```bash
SERVICE="$1"
systemctl restart "$SERVICE"
```

when the requirement calls for a reusable tool.

## 5. Did I handle failure?

Production scripts should not assume commands always succeed.

Use:

```bash
if
```

exit codes, logging, and appropriate `exit` values.

---

# Final Goal

You do **not** need to memorize this document.

The skill you are trying to develop is:

> "I may not remember the exact command, but I know what information I need, what part of Linux I should investigate, what kind of command will give me that information, and how I will verify the result."

That is the beginning of real Linux and DevOps troubleshooting skill.
