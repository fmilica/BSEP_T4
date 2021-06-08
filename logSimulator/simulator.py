import sys
from log import Log
from random import choice
import json
from datetime import datetime, timezone


def main():
    file_path = sys.argv[1]
    mode = sys.argv[2]
    if mode == "regular":
        regular_mode(file_path)
    elif mode == "attack":
        attack_type = sys.argv[3]
        attack_mode(file_path, attack_type)
    elif mode == "error":
        error_mode(file_path)


def regular_mode(file_path):
    dt1 = datetime.now(timezone.utc).astimezone().isoformat()
    list_date = dt1.split('+')
    list_date[0] = list_date[0][:-3]
    dt1 = (list_date[0]) + '+' + (list_date[1])
    users = ["640d99cc-3299-4a3a-a170-dc3ebabf6775", "e2c8c5b2-aee0-48e3-ac9b-e1fbb97ff15d",
             "c82402f4-5d9a-4c3b-b36e-30be69289536", "86ae6f8a-d22e-416a-998b-98b788ae8c91"]
    messages = ["CSR created.", "Certificate created.", "Message log."]
    f = open(file_path, "a")
    # log = Log(dt1, "INFO", choice(messages), choice(users),
    #	  "", "127.0.0.1", "", 200, False)
    log = Log(dt1,
              "userId={}, ipAddress={}, statusCode={}".format(choice(users), "127.0.0.1",
                                                                                 "200"), "INFO")
    f.write(json.dumps(log.__dict__) + '\n')
    f.close()


def attack_mode(file_path, attack_type):
    ch = [0, 1]


    error = ["LOGIN_ERROR", "LOGIN"]
    malicious_ips = load_malicious_ips()
    not_malicious_ips = ["128.12.2.2", "111.1.1.1", "20.2.0.9", "90.6.0.1", "117.2.2.1", "100.1.100.1", "102.1.1.1"]
    users = ["admin_mika", "doktor_pera", "user1"]
    f = open(file_path, "a")

    if attack_type == "malicious_ip_logs":
        dt1 = datetime.now(timezone.utc).astimezone().isoformat()
        list_date = dt1.split('+')
        list_date[0] = list_date[0][:-3]
        dt1 = (list_date[0]) + '+' + (list_date[1])
        log = Log(dt1, "userId={}, ipAddress={}, statusCode={}".format(choice(users), 
                                                                                          choice(malicious_ips), "200"),
                  "INFO")
        f.write(json.dumps(log.__dict__) + '\n')
    elif attack_type == "malicious_ip_login_logs":
        dt1 = datetime.now(timezone.utc).astimezone().isoformat()
        list_date = dt1.split('+')
        list_date[0] = list_date[0][:-3]
        dt1 = (list_date[0]) + '+' + (list_date[1])
        # log = Log(dt1, "WARN", "Keycloak logging user.", choice(users),
        # choice(error), choice(malicious_ips), "invalid_user_credentials", 404, False)
        if (choice(ch)):
            log = Log(dt1, "userId={}, type={}, ipAddress={}, statusCode={}".format(choice(users), "LOGIN",
                                                                                              choice(malicious_ips), 
                                                                                              "200"), "INFO")
            f.write(json.dumps(log.__dict__) + '\n')
        else:
            log = Log(dt1, "userId={}, type={}, ipAddress={}, error={}, statusCode={}".format(choice(users), "LOGIN_ERROR",
                                                                                              choice(malicious_ips),
                                                                                              "invalid_user_credentials",
                                                                                              "400"), "WARN")
            f.write(json.dumps(log.__dict__) + '\n')
    elif attack_type == "new_malicious_ip":
        ip = choice(not_malicious_ips)
        hours = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]
        for i in range(31):
            dt1 = datetime.now(timezone.utc).replace(hour=choice(hours)).astimezone().isoformat()
            list_date = dt1.split('+')
            list_date[0] = list_date[0][:-3]
            dt1 = (list_date[0]) + '+' + (list_date[1])
            # log = Log(dt1, "WARN", "Keycloak logging user.", choice(users),
            #         "LOGIN_ERROR", ip, "invalid_user_credentials", 404, False)
            log = Log(dt1, "userId={}, type={}, ipAddress={}, error={}, statusCode={}".format(choice(users), "LOGIN_ERROR",
                                                                                              choice(not_malicious_ips),
                                                                                              "invalid_user_credentials",
                                                                                              "400"), "WARN")
            f.write(json.dumps(log.__dict__) + '\n')
    elif attack_type == "failed_login":
        username = choice(users)
        for i in range(6):
            dt1 = datetime.now(timezone.utc).astimezone().replace(hour=10).isoformat()
            list_date = dt1.split('+')
            list_date[0] = list_date[0][:-3]
            dt1 = (list_date[0]) + '+' + (list_date[1])
            # log = Log(dt1, "WARN", "Keycloak logging user.", username,
            #      "LOGIN_ERROR", "127.0.0.1", "invalid_user_credentials", 404, False)
            log = Log(dt1, "userId={}, type={}, ipAddress={}, error={}, statusCode={}".format(username, "LOGIN_ERROR",
                                                                                          "127.0.0.1",
                                                                                          "invalid_user_credentials",
                                                                                          "400"), "WARN")
            f.write(json.dumps(log.__dict__) + '\n')
    elif attack_type == "frequent_login_logs":
        for i in range(11):
            dt1 = datetime.now(timezone.utc).astimezone().replace(hour=11, minute=14, second=i).isoformat()
            list_date = dt1.split('+')
            list_date[0] = list_date[0][:-3]
            dt1 = (list_date[0]) + '+' + (list_date[1])
            # log = Log(dt1, "WARN", "Keycloak logging user.", choice(users),
            #  choice(error), "127.0.0.1", "", 200, False)
            if (choice(ch)):
                log = Log(dt1, "userId={}, type={}, ipAddress={}, statusCode={}".format(choice(users), "LOGIN",
                                                                                              "127.0.0.1", "200"),
                                                                                              "INFO")
                f.write(json.dumps(log.__dict__) + '\n')
            else:
                log = Log(dt1, "userId={}, type={}, ipAddress={}, error={}, statusCode={}".format(choice(users), "LOGIN_ERROR",
                                                                                              "127.0.0.1",
                                                                                              "invalid_user_credentials",
                                                                                              "400"), "WARN")
                f.write(json.dumps(log.__dict__) + '\n')
    elif attack_type == "frequent_other_logs":
            for i in range(51):
                dt1 = datetime.now(timezone.utc).astimezone().replace(hour=10, minute=4, second=i).isoformat()
                list_date = dt1.split('+')
                list_date[0] = list_date[0][:-3]
                dt1 = (list_date[0]) + '+' + (list_date[1])
                # log = Log(dt1, "INFO", "Message for log.", choice(users),
                #    "", "127.0.0.1", "", 200, False)
                log = Log(dt1,
                      "userId={}, ipAddress={}, statusCode={}".format(choice(users), "127.0.0.1",
                                                                                         "200"), "INFO")
                f.write(json.dumps(log.__dict__) + '\n')
    f.close()


def error_mode(file_path):
    dt1 = datetime.now(timezone.utc).astimezone().isoformat()
    list_date = dt1.split('+')
    list_date[0] = list_date[0][:-3]
    dt1 = (list_date[0]) + '+' + (list_date[1])
    # log = Log(dt1, "ERROR", "Error occured.", "640d99cc-3299-4a3a-a170-dc3ebabf6775",
    #     "", "127.0.0.1", "", 404, False)
    log = Log(dt1, "userId={}, ipAddress={}, statusCode={}".format("640d99cc-3299-4a3a-a170-dc3ebabf6775", "127.0.0.1", "404"), "ERROR")
    f = open(file_path, "a")
    f.write(json.dumps(log.__dict__) + '\n')
    f.close()


def load_malicious_ips():
    mips = []
    f = open("malicious_ip_addresses.txt", "r")
    for i in f.readlines():
        mips.append(i.strip())
    return mips


if __name__ == "__main__":
    main()
