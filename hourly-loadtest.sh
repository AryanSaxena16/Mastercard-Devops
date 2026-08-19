#!/bin/bash

# Multi-currency profile registry array
currencies=("INR" "USD" "JPY" "RUB" "EUR")

# Production Automation Step: Generate a dynamic hourly seed to prevent Idempotency collisions
current_hour=$(date +%Y%m%d%H)

echo "[$(date '+%Y-%m-%d %H:%M:%S')] START: Executing automated load-test for hour cluster: $current_hour" >> /root/cron-output.log

for i in {1..50}; do
  # Pick a currency index dynamically based on the loop count
  curr=${currencies[$((i % 5))]}

  # Randomly assign a failure or delay parameter to variations for test diversity
  delay=0
  fail="false"
  if [ $((i % 10)) -eq 0 ]; then delay=3000; fi # Triggers our 2s timeout rule
  if [ $((i % 15)) -eq 0 ]; then fail="true"; fi # Triggers explicit gateway failure

  # Structural Variables for Auditing
  tx_key="tx-cron-$current_hour-$i"
  amount="100.$i"

  # Log the immediate PENDING status to the transaction audit file BEFORE making the network call
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] PENDING | Key: $tx_key | Cur: $curr | Amt: $amount | Delay: ${delay}ms | FailSim: $fail" >> /root/transaction-audit.log

  # Execute the payment via a non-blocking background subshell wrapper to log responses asynchronously
  (
    # Make the API call, capture the raw response code, and append to the audit log upon completion
    http_code=$(curl -s -o /dev/null -w "%{http_code}" \
      -X POST http://localhost:8080/api/v1/payments \
      -H "Content-Type: application/json" \
      -d "{\"idempotencyKey\": \"$tx_key\", \"amount\": $amount, \"currency\": \"$curr\", \"simulateDelayMs\": $delay, \"simulateFailure\": $fail}")

    echo "[$(date '+%Y-%m-%d %H:%M:%S')] FINISHED | Key: $tx_key | Cur: $curr | Amt: $amount | HTTP_STATUS: $http_code" >> /root/transaction-audit.log
  ) &
done

# Wait for all asynchronous background workers to resolve
wait
echo "[$(date '+%Y-%m-%d %H:%M:%S')] END: Completed 50 transaction injections for hour cluster $current_hour" >> /root/cron-output.log
