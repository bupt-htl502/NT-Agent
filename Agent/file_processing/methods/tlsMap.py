versionMap = {
    "0x0304": "tls13",
    "0x0303": "tls12",
    "0x0301": "tls10",
    "0xfefc": "dtls13",
    "0xfefd": "dtls12",
    "0xfeff": "dtls10"
}

cipherSet = {"0x0000",
             "0x0001",
             "0x0002",
             "0x0003",
             "0x0004",
             "0x0005",
             "0x0006",
             "0x0007",
             "0x0008",
             "0x0009",
             "0x000a",
             "0x000b",
             "0x000c",
             "0x000d",
             "0x000e",
             "0x000f",
             "0x0010",
             "0x0011",
             "0x0012",
             "0x0013",
             "0x0014",
             "0x0015",
             "0x0016",
             "0x0017",
             "0x0018",
             "0x0019",
             "0x001a",
             "0x001b",
             "0x001e",
             "0x001f",
             "0x0020",
             "0x0021",
             "0x0022",
             "0x0023",
             "0x0024",
             "0x0025",
             "0x0026",
             "0x0027",
             "0x0028",
             "0x0029",
             "0x002a",
             "0x002b",
             "0x002c",
             "0x002d",
             "0x002e",
             "0x002f",
             "0x0030",
             "0x0031",
             "0x0032",
             "0x0033",
             "0x0034",
             "0x0035",
             "0x0036",
             "0x0037",
             "0x0038",
             "0x0039",
             "0x003a",
             "0x003b",
             "0x003c",
             "0x003d",
             "0x003e",
             "0x003f",
             "0x0040",
             "0x0041",
             "0x0042",
             "0x0043",
             "0x0044",
             "0x0045",
             "0x0046",
             "0x0067",
             "0x0068",
             "0x0069",
             "0x006a",
             "0x006b",
             "0x006c",
             "0x006d",
             "0x0084",
             "0x0085",
             "0x0086",
             "0x0087",
             "0x0088",
             "0x0089",
             "0x008a",
             "0x008b",
             "0x008c",
             "0x008d",
             "0x008e",
             "0x008f",
             "0x0090",
             "0x0091",
             "0x0092",
             "0x0093",
             "0x0094",
             "0x0095",
             "0x0096",
             "0x0097",
             "0x0098",
             "0x0099",
             "0x009a",
             "0x009b",
             "0x009c",
             "0x009d",
             "0x009e",
             "0x009f",
             "0x00a0",
             "0x00a1",
             "0x00a2",
             "0x00a3",
             "0x00a4",
             "0x00a5",
             "0x00a6",
             "0x00a7",
             "0x00a8",
             "0x00a9",
             "0x00aa",
             "0x00ab",
             "0x00ac",
             "0x00ad",
             "0x00ae",
             "0x00af",
             "0x00b0",
             "0x00b1",
             "0x00b2",
             "0x00b3",
             "0x00b4",
             "0x00b5",
             "0x00b6",
             "0x00b7",
             "0x00b8",
             "0x00b9",
             "0x00ba",
             "0x00bb",
             "0x00bc",
             "0x00bd",
             "0x00be",
             "0x00bf",
             "0x00c0",
             "0x00c1",
             "0x00c2",
             "0x00c3",
             "0x00c4",
             "0x00c5",
             "0x00c6",
             "0x00c7",
             "0x00ff",
             "0x1301",
             "0x1302",
             "0x1303",
             "0x1304",
             "0x1305",
             "0x1306",
             "0x1307",
             "0x5600",
             "0xc001",
             "0xc002",
             "0xc003",
             "0xc004",
             "0xc005",
             "0xc006",
             "0xc007",
             "0xc008",
             "0xc009",
             "0xc00a",
             "0xc00b",
             "0xc00c",
             "0xc00d",
             "0xc00e",
             "0xc00f",
             "0xc010",
             "0xc011",
             "0xc012",
             "0xc013",
             "0xc014",
             "0xc015",
             "0xc016",
             "0xc017",
             "0xc018",
             "0xc019",
             "0xc01a",
             "0xc01b",
             "0xc01c",
             "0xc01d",
             "0xc01e",
             "0xc01f",
             "0xc020",
             "0xc021",
             "0xc022",
             "0xc023",
             "0xc024",
             "0xc025",
             "0xc026",
             "0xc027",
             "0xc028",
             "0xc029",
             "0xc02a",
             "0xc02b",
             "0xc02c",
             "0xc02d",
             "0xc02e",
             "0xc02f",
             "0xc030",
             "0xc031",
             "0xc032",
             "0xc033",
             "0xc034",
             "0xc035",
             "0xc036",
             "0xc037",
             "0xc038",
             "0xc039",
             "0xc03a",
             "0xc03b",
             "0xc03c",
             "0xc03d",
             "0xc03e",
             "0xc03f",
             "0xc040",
             "0xc041",
             "0xc042",
             "0xc043",
             "0xc044",
             "0xc045",
             "0xc046",
             "0xc047",
             "0xc048",
             "0xc049",
             "0xc04a",
             "0xc04b",
             "0xc04c",
             "0xc04d",
             "0xc04e",
             "0xc04f",
             "0xc050",
             "0xc051",
             "0xc052",
             "0xc053",
             "0xc054",
             "0xc055",
             "0xc056",
             "0xc057",
             "0xc058",
             "0xc059",
             "0xc05a",
             "0xc05b",
             "0xc05c",
             "0xc05d",
             "0xc05e",
             "0xc05f",
             "0xc060",
             "0xc061",
             "0xc062",
             "0xc063",
             "0xc064",
             "0xc065",
             "0xc066",
             "0xc067",
             "0xc068",
             "0xc069",
             "0xc06a",
             "0xc06b",
             "0xc06c",
             "0xc06d",
             "0xc06e",
             "0xc06f",
             "0xc070",
             "0xc071",
             "0xc072",
             "0xc073",
             "0xc074",
             "0xc075",
             "0xc076",
             "0xc077",
             "0xc078",
             "0xc079",
             "0xc07a",
             "0xc07b",
             "0xc07c",
             "0xc07d",
             "0xc07e",
             "0xc07f",
             "0xc080",
             "0xc081",
             "0xc082",
             "0xc083",
             "0xc084",
             "0xc085",
             "0xc086",
             "0xc087",
             "0xc088",
             "0xc089",
             "0xc08a",
             "0xc08b",
             "0xc08c",
             "0xc08d",
             "0xc08e",
             "0xc08f",
             "0xc090",
             "0xc091",
             "0xc092",
             "0xc093",
             "0xc094",
             "0xc095",
             "0xc096",
             "0xc097",
             "0xc098",
             "0xc099",
             "0xc09a",
             "0xc09b",
             "0xc09c",
             "0xc09d",
             "0xc09e",
             "0xc09f",
             "0xc0a0",
             "0xc0a1",
             "0xc0a2",
             "0xc0a3",
             "0xc0a4",
             "0xc0a5",
             "0xc0a6",
             "0xc0a7",
             "0xc0a8",
             "0xc0a9",
             "0xc0aa",
             "0xc0ab",
             "0xc0ac",
             "0xc0ad",
             "0xc0ae",
             "0xc0af",
             "0xc0b0",
             "0xc0b1",
             "0xc0b2",
             "0xc0b3",
             "0xc0b4",
             "0xc0b5",
             "0xc100",
             "0xc101",
             "0xc102",
             "0xc103",
             "0xc104",
             "0xc105",
             "0xc106",
             "0xcca8",
             "0xcca9",
             "0xccaa",
             "0xccab",
             "0xccac",
             "0xccad",
             "0xccae",
             "0xd001",
             "0xd002",
             "0xd003",
             "0xd005"
             }

supportedGroupMap = {
    "0x0001": "sect163k1",
    "0x0002": "sect163r1",
    "0x0003": "sect163r2",
    "0x0004": "sect193r1",
    "0x0005": "sect193r2",
    "0x0006": "sect233k1",
    "0x0007": "sect233r1",
    "0x0008": "sect239k1",
    "0x0009": "sect283k1",
    "0x000a": "sect283r1",
    "0x000b": "sect409k1",
    "0x000c": "sect409r1",
    "0x000d": "sect571k1",
    "0x000e": "sect571r1",
    "0x000f": "secp160k1",
    "0x0010": "secp160r1",
    "0x0011": "secp160r2",
    "0x0012": "secp192k1",
    "0x0013": "secp192r1",
    "0x0014": "secp224k1",
    "0x0015": "secp224r1",
    "0x0016": "secp256r1",
    "0x0017": "secp256r1",
    "0x0018": "secp384r1",
    "0x0019": "secp521r1",
    "0x001a": "brainpoolP256r1",
    "0x001b": "brainpoolP384r1",
    "0x001c": "brainpoolP512r1",
    "0x001d": "x25519",
    "0x001e": "x448",
    "0x001f": "brainpoolP256r1tls13",
    "0x0020": "brainpoolP384r1tls13",
    "0x0021": "brainpoolP512r1tls13",
    "0x0022": "GC256A",
    "0x0023": "GC256B",
    "0x0024": "GC256C",
    "0x0025": "GC256D",
    "0x0026": "GC512A",
    "0x0027": "GC512B",
    "0x0028": "GC512C",
    "0x0029": "curveSM2",
    "0x0100": "ffdhe2048",
    "0x0101": "ffdhe3072",
    "0x0102": "ffdhe4096",
    "0x0103": "ffdhe6144",
    "0x0104": "ffdhe8192",
    "0x11eb": "SecP256r1MLKEM768",
    "0x11ec": "X25519MLKEM768",
    "0x6399": "X25519Kyber768Draft00",
    "0x639a": "SecP256r1Kyber768Draft00",
    "0xff01": "arbitrary_explicit_prime_curves",
    "0xff02": "arbitrary_explicit_char2_curves",
    "0x0a0a": "GREASE0",
    "0x1a1a": "GREASE1",
    "0x2a2a": "GREASE2",
    "0x3a3a": "GREASE3",
    "0x4a4a": "GREASE4",
    "0x5a5a": "GREASE5",
    "0x6a6a": "GREASE6",
    "0x7a7a": "GREASE7",
    "0x8a8a": "GREASE8",
    "0x9a9a": "GREASE9",
    "0xaaaa": "GREASEa",
    "0xbaba": "GREASEb",
    "0xcaca": "GREASEc",
    "0xdada": "GREASEd",
    "0xeaea": "GREASEe",
    "0xfafa": "GREASEf"
}

ecPointFormatMap = {
    "0": "uncompressed",
    "1": "ansiX962compressedprime",
    "2": "ansiX962compressedchar2"
}

sigMap = {
    "00": "anonymous",
    "01": "rsa",
    "02": "dsa",
    "03": "ecdsa",
    "07": "ed25519",
    "08": "ed448",
    "40": "gostr34102012c256",
    "41": "gostr34102012c512"
}

hashAlgMap = {
    "00": "none",
    "01": "md5",
    "02": "sha1",
    "03": "sha224",
    "04": "sha256",
    "05": "sha384",
    "06": "sha512",
    "08": "Intrinsic"
}


def version2str(versions):
    result = ""
    for field in versions.fields:
        if field.show in versionMap:
            result += '|' + versionMap[field.show]
        else:
            # result += '|' + 'GREASE' + '(' + field.show + ')'  # 该种方式更直观
            result += '|' + field.show  # 该种方式更适合小模型小词表
    result = result[1:]
    # print(result)
    return result


def cipher2str(ciphers):
    result = ""
    for field in ciphers.fields:
        if field.show in cipherSet:
            result += '|' + field.show
        else:
            # result += '|' + 'GREASE' + '(' + field.show + ')'  # 该种方式更直观
            result += '|' + field.show  # 该种方式更适合小模型小词表
    result = result[1:]
    # print(result)
    return result


def extensionCount(layer):
    result = 0
    for field_line in layer._get_all_field_lines():
        # 选取“(len”作为Extension的筛选字符，Extension本身可能出现在其嵌套内容中，不适合使用
        if '(len=' in field_line:
            result += 1
    # print(result)
    return result


def supportedGroup2str(suppGroups):
    result = ""
    for field in suppGroups.fields:
        if field.show in supportedGroupMap:
            result += '|' + supportedGroupMap[field.show]
        else:
            result += '|' + 'unknown supported Group' + '(' + field.show + ')'
    result = result[1:]
    # print(result)
    return result


def ecPointFormat2str(ec):
    result = ""
    for field in ec.fields:
        if field.show in ecPointFormatMap:
            result += '|' + ecPointFormatMap[field.show]
        else:
            result += '|' + 'unknown ec point format' + '(' + field.show + ')'
    result = result[1:]
    # print(result)
    return result


def sigHashAlg2str(sh):
    result = ""
    for field in sh.fields:
        sig = field.show[4:]
        hashAlg = field.show[2:4]
        tmp = ""
        if sig in sigMap:
            tmp += sigMap[sig] + ' '
        else:
            tmp += f'unknown{sig} '
        if hashAlg in hashAlgMap:
            tmp += hashAlgMap[hashAlg]
        else:
            tmp += f'unknown{hashAlg}'
        result += '|' + tmp
    result = result[1:]
    # print(result)
    return result


def issuer2str(rdc, num):
    result = ""
    cnt = 0
    cn = []
    for field in rdc:
        if 'commonName' in field.showname:
            cn.append(field)
    for cnt in range(num):
        field = cn[cnt]
        if 'commonName' in field.showname:
            parts = field.showname.split('=')
            content_after_equals = parts[1].strip().strip('()')
            result += '|' + content_after_equals
        cnt += 1
    result = result[1:]
    # print(result)
    return result


def subject2str(rdc, num):
    result = ""
    cnt = 0
    cn = []
    for field in rdc:
        if 'commonName' in field.showname:
            cn.append(field)
    for cnt in range(num):
        field = cn[cnt + num]
        if 'commonName' in field.showname:
            parts = field.showname.split('=')
            content_after_equals = parts[1].strip().strip('()')
            result += '|' + content_after_equals
        cnt += 1
    result = result[1:]
    # print(result)
    return result


def notBefore2str(val):
    result = ''
    l = int(len(val) / 2)
    for i in range(l):
        field = val[i * 2]
        parts = field.showname.split('Time: ')
        content_after_equals = parts[1].strip()
        result += '|' + content_after_equals
    result = result[1:]
    # print(result)
    return result


def notAfter2str(val):
    result = ''
    l = int(len(val) / 2)
    for i in range(l):
        field = val[i * 2 + 1]
        parts = field.showname.split('Time: ')
        content_after_equals = parts[1].strip()
        result += '|' + content_after_equals
    result = result[1:]
    # print(result)
    return result


def str2hex(ori_data, hex_len):
    result = ''
    str_ori = ori_data.show
    str_len = len(str_ori)
    # print(f"str len: {len(str_ori)}, str type: {type(str_ori)}, hex_len: {hex_len}")
    # print(str_ori)
    for i in range(hex_len):
        # pyshark的十六进制内容格式为"ab:cd:..."，双字节按照两个冒号的内容进行分割共计每次处理六个字符
        if 6*i+4 <= str_len:
            # 双字节均有效
            hex_i = str_ori[6*i:6*i+2] + str_ori[6*i+3: 6*i+5]
            result += ' ' + hex_i
        elif 6*i+1 <= str_len:
            # 仅有单字节有效，后续需要补00
            hex_i = str_ori[6*i:6*i+2] + '00'
            result += ' ' + hex_i
        else:
            # 长度不足设定的hex_len，用0000进行补充
            hex_i = '0000'
            result += ' ' + hex_i
    result = result[1:]
    return result


def encrypted_handshake_hex(raw, pos, length):
    """
    生成十六进制编码字符串
    :param raw: 待处理的原始数据，类型为byte
    :param pos: 密文位置
    :param length: 密文长度
    :return: 划分后的十六进制编码字符串
    """
    pkt_hex = ''
    for byte in raw:
        hex_byte = f'{byte:02x}'
        pkt_hex += hex_byte
    # print(pkt_hex)
    result = pkt_hex[pos*2: pos*2 + 2*length]
    # print(result)
    return result


def str2hex1(hex_string, hex_len):
    """
    将原始密文十六进制编码字符串转化为十六进制双字节字符串(增加空格进行划分和填充)
    :param hex_string:原始密文十六进制编码字符串
    :param hex_len:密文长度
    :return:十六进制双字节字符串
    """
    result = ''
    str_len = len(hex_string)
    for i in range(hex_len):
        # 对字节范围进行判断，避免溢出
        if 4 * i < str_len:
            # 双字节均有效
            hex_i = hex_string[4*i:4*i+4]
            result += ' ' + hex_i
        elif 4*i+2 < str_len:
            # 仅单字节有效
            hex_i = hex_string[4*i: 4*i+2] + '00'
            result += ' ' + hex_i
        else:
            # 进行填充
            hex_i = '0000'
            result += ' ' + hex_i
    result = result[1:]  # 去除第一个空格
    return result
